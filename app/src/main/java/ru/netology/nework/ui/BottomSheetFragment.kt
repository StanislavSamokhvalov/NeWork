package ru.netology.nework.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.nework.R
import ru.netology.nework.adapter.UserCallback
import ru.netology.nework.adapter.UsersAdapter
import ru.netology.nework.databinding.FragmentBottomSheetBinding
import ru.netology.nework.databinding.FragmentUsersBinding
import ru.netology.nework.dto.User
import ru.netology.nework.viewmodel.UserViewModel

@AndroidEntryPoint
class BottomSheetFragment : BottomSheetDialogFragment() {

    private val userViewModel: UserViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentBottomSheetBinding.inflate(inflater, container, false)
        val adapter = UsersAdapter(object : UserCallback {
            override fun onUser(user: User) {
                val bundle = Bundle().apply {
                    putInt("id", user.id)
                    putString("name", user.name)
                    putString("avatar", user.avatar)
                }
                findNavController().navigate(R.id.navigation_profile, bundle)
            }
        })
        binding.list.adapter = adapter

        userViewModel.data.observe(viewLifecycleOwner) { userModel ->
            adapter.submitList(userModel.users.filter { user ->
                userViewModel.userIds.value!!.contains(user.id)
            })
        } //TODO("Падаем с ошибкой NPE из onSpeakers и всех производных getuserids")

        return binding.root
    }
}