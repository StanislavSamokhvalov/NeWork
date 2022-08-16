package ru.netology.nework.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.nework.R
import ru.netology.nework.auth.AppAuth
import ru.netology.nework.databinding.FragmentProfileBinding
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
    private val postViewModel: PostViewModel by viewModels()
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentProfileBinding.inflate(inflater, container, false)

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

        with(binding) {
            logout.visibility = View.VISIBLE
//            editAvatar.visibility = View.VISIBLE
//
//            editAvatar.setOnClickListener {
//
//            }

            logout.setOnClickListener {
                appAuth.removeAuth()
                findNavController().navigate(R.id.action_navigation_profile_to_signInFragment)
            }
        }

        return binding.root
    }
}