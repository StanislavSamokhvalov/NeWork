package ru.netology.nework.ui

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import ru.netology.nework.R
import ru.netology.nework.adapter.JobCallback
import ru.netology.nework.adapter.JobsAdapter
import ru.netology.nework.adapter.PostCallback
import ru.netology.nework.adapter.PostsAdapter
import ru.netology.nework.auth.AppAuth
import ru.netology.nework.databinding.FragmentProfileBinding
import ru.netology.nework.util.AndroidUtils.uploadingAvatar
import ru.netology.nework.util.AndroidUtils.uploadingAvatarBackground
import ru.netology.nework.viewmodel.JobViewModel
import ru.netology.nework.viewmodel.PostViewModel
import ru.netology.nework.viewmodel.UserViewModel
import javax.inject.Inject


@AndroidEntryPoint
class UserProfileFragment : Fragment() {
    @Inject
    lateinit var appAuth: AppAuth

    private val userViewModel: UserViewModel by viewModels()
    private val jobViewModel: JobViewModel by viewModels()
    private val postViewModel: PostViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentProfileBinding.inflate(inflater, container, false)

        with(binding) {
            val newLayoutParams = mainContainer.getLayoutParams()

            val pxValue = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                700F,
                context?.getResources()?.getDisplayMetrics()
            ).toInt()

            newLayoutParams.height = pxValue
            mainContainer.setLayoutParams(newLayoutParams)
        }

        val avatar = arguments?.getString("url")
        val name = arguments?.getString("name")

        userViewModel.user.observe(viewLifecycleOwner) {
            with(binding) {
                userName.text = name
                uploadingAvatar(userAvatar, avatar)
                uploadingAvatarBackground(background, avatar)
                userAvatar.setOnClickListener {
                    val bundle = Bundle().apply {
                        putString("url", avatar)
                    }
                    findNavController().navigate(R.id.singleImageFragment, bundle)
                }
            }
        }

        val jobAdapter = JobsAdapter(object : JobCallback {})
        binding.job.adapter = jobAdapter

        lifecycleScope.launchWhenCreated {
            jobViewModel.loadJobs()
            jobViewModel.data.collectLatest {
                jobAdapter.submitList(it)
            }
        }

        val postsAdapter = PostsAdapter(object : PostCallback {})
        binding.posts.adapter = postsAdapter

        binding.swiperefresh.setOnRefreshListener {
            postViewModel.refreshPosts()
            jobViewModel.refreshJobs()
        }

        postViewModel.dataState.observe(viewLifecycleOwner) { state ->
            binding.swiperefresh.isRefreshing = state.refreshing
            if (state.error) {
                Snackbar.make(binding.root, R.string.error_loading, Snackbar.LENGTH_LONG).show()
            }
        }

        jobViewModel.dataState.observe(viewLifecycleOwner) { state ->
            binding.swiperefresh.isRefreshing = state.refreshing
            if (state.error) {
                Snackbar.make(binding.root, R.string.error_loading, Snackbar.LENGTH_LONG).show()
            }
        }

        lifecycleScope.launchWhenCreated {
            postViewModel.userWall.collectLatest {
                postsAdapter.submitData(it)
            }
        }

        return binding.root
    }
}