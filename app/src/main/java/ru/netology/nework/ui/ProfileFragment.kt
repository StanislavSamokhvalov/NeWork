package ru.netology.nework.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.github.dhaval2404.imagepicker.ImagePicker
import com.github.dhaval2404.imagepicker.constant.ImageProvider
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.coroutines.flow.collectLatest
import ru.netology.nework.R
import ru.netology.nework.adapter.JobCallback
import ru.netology.nework.adapter.JobsAdapter
import ru.netology.nework.adapter.PostCallback
import ru.netology.nework.adapter.PostsAdapter
import ru.netology.nework.auth.AppAuth
import ru.netology.nework.databinding.FragmentProfileBinding
import ru.netology.nework.dto.Post
import ru.netology.nework.util.AndroidUtils.uploadingAvatar
import ru.netology.nework.util.AndroidUtils.uploadingAvatarBackground
import ru.netology.nework.viewmodel.AuthViewModel
import ru.netology.nework.viewmodel.JobViewModel
import ru.netology.nework.viewmodel.PostViewModel
import ru.netology.nework.viewmodel.UserViewModel
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    @Inject
    lateinit var appAuth: AppAuth

    private val userViewModel: UserViewModel by viewModels()
    private val jobViewModel: JobViewModel by viewModels()
    private val postViewModel: PostViewModel by activityViewModels()
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentProfileBinding.inflate(inflater, container, false)

        val pickPhotoLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                when (it.resultCode) {
                    ImagePicker.RESULT_ERROR -> {
                        Snackbar.make(
                            binding.root,
                            ImagePicker.getError(it.data),
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                    Activity.RESULT_OK -> {
                        val uri: Uri? = it.data?.data
                        userViewModel.changePhoto(uri)
                    }
                }
            }

        userViewModel.user.observe(viewLifecycleOwner) { user ->
            with(binding) {
                userName.text = user.name
                uploadingAvatar(userAvatar, user.avatar)
                uploadingAvatarBackground(background, user.avatar)
                userAvatar.setOnClickListener {
                    val bundle = Bundle().apply {
                        putString("url", user.avatar)
                    }
                    findNavController().navigate(R.id.singleImageFragment, bundle)
                }
            }
        }

        val jobAdapter = JobsAdapter(object : JobCallback{})
        binding.job.adapter = jobAdapter

        lifecycleScope.launchWhenCreated {
            jobViewModel.loadJobs()
            jobViewModel.data.collectLatest {
                jobAdapter.submitList(it)
            }
        }

        val postsAdapter = PostsAdapter(object : PostCallback {
            override fun onEdit(post: Post) {
                postViewModel.edit(post)
                val bundle = Bundle().apply {
                    putString("content", post.content)
                }
                findNavController().navigate(R.id.newPostFragment, bundle)
            }

            override fun onLike(post: Post) {
                if (authViewModel.authenticated) {
                    if (!post.likedByMe) postViewModel.likeById(post.id) else postViewModel.unlikeById(
                        post.id
                    )
                } else findNavController().navigate(R.id.action_feedFragment_to_signInFragment)
            }

            override fun onRemove(post: Post) {
                postViewModel.removeById(post.id)
            }

            override fun onShare(post: Post) {
                val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, post.content)
                    type = "text/plain"
                }

                val shareIntent =
                    Intent.createChooser(intent, getString(R.string.chooser_share_post))
                startActivity(shareIntent)
            }

            override fun onHide(post: Post) {
                postViewModel.hidePost(post)
            }

            override fun onAudio(post: Post) {
                try {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(post.attachment?.url))
                    val audioIntent =
                        Intent.createChooser(intent, getString(R.string.media_chooser))
                    startActivity(audioIntent)
                } catch (e: Exception) {
                    Toast.makeText(context, R.string.error_play_audio, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onVideo(post: Post) {
                try {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(post.attachment?.url))
                    val videoIntent =
                        Intent.createChooser(intent, getString(R.string.media_chooser))
                    startActivity(videoIntent)
                } catch (e: Exception) {
                    Toast.makeText(context, R.string.error_play_video, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onOpenImage(post: Post) {
                val bundle = Bundle().apply {
                    putString("url", post.attachment?.url)
                }
                findNavController().navigate(R.id.singleImageFragment, bundle)
            }

            override fun onOpenAvatar(post: Post) {
                val bundle = Bundle().apply {
                    putString("url", post.authorAvatar)
                }
                findNavController().navigate(R.id.singleImageFragment, bundle)
            }
        })
        binding.posts.adapter = postsAdapter

        lifecycleScope.launchWhenCreated {
            postViewModel.userWall.collectLatest {
                postsAdapter.submitData(it)
            }
        }

        with(binding) {
            logout.visibility = View.VISIBLE
            editAvatar.visibility = View.VISIBLE
            addJob.visibility = View.VISIBLE

            logout.setOnClickListener {
                appAuth.removeAuth()
                findNavController().navigate(R.id.action_navigation_profile_to_signInFragment)
            }

            editAvatar.setOnClickListener {
                ImagePicker.with(this@ProfileFragment)
                    .cropSquare()
                    .compress(2048)
                    .provider(ImageProvider.GALLERY)
                    .createIntent(pickPhotoLauncher::launch)
            }

            addJob.setOnClickListener {
                findNavController().navigate(R.id.action_navigation_profile_to_newJobFragment)
            }
        }

        return binding.root
    }
}