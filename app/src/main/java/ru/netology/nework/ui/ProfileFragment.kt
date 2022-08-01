package ru.netology.nework.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.nework.databinding.FragmentProfileBinding
import ru.netology.nework.util.uploadingAvatar
import ru.netology.nework.viewmodel.UserViewModel

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private val userViewModel: UserViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentProfileBinding.inflate(inflater, container, false)

        userViewModel.user.observe(viewLifecycleOwner) {
            with(binding) {
                authorName.text = it.name
                uploadingAvatar(avatar, it.avatar)
            }
        }
//        with(binding) {
//            userName.text = name
//            userAvatar.setOnClickListener {
//                val bundle = Bundle().apply {
//                    putString("url", avatar)
//                }
//                findNavController().navigate(R.id.imageFragment, bundle)
//            }

        return binding.root
    }
}