package ru.netology.nework.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.card_event.*
import ru.netology.nework.R
import ru.netology.nework.auth.AppAuth
import ru.netology.nework.databinding.FragmentProfileBinding
import ru.netology.nework.util.AndroidUtils.uploadingAvatar
import ru.netology.nework.viewmodel.UserViewModel
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    @Inject
    lateinit var appAuth: AppAuth

    private val userViewModel: UserViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentProfileBinding.inflate(inflater, container, false)
//        val userId = arguments?.getString("id")
//        val avatar = arguments?.getString("url")
//        val userName = arguments?.getString("name")

//        userAvatar.setOnClickListener {
//            val bundle = Bundle().apply {
//                putString("url", avatar)
//            }
//            findNavController().navigate(R.id.singleImageFragment, bundle)
//        }

        userViewModel.user.observe(viewLifecycleOwner) {
            with(binding) {
                userName.text = it.name
                uploadingAvatar(userAvatar, it.avatar)
            }
        }


        binding.logout.setOnClickListener {
            appAuth.removeAuth()
            findNavController().navigate(R.id.action_navigation_profile_to_signInFragment)
        }

        return binding.root
    }
}