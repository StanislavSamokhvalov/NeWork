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
class UserProfileFragment : Fragment() {
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

        val id = arguments?.getInt("id")
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

        return binding.root
    }
}