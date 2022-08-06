package ru.netology.nework.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.nework.adapter.UserCallback
import ru.netology.nework.adapter.UsersAdapter
import ru.netology.nework.databinding.FragmentUsersBinding
import ru.netology.nework.viewmodel.UserViewModel

@AndroidEntryPoint
class BottomSheetFragment : BottomSheetDialogFragment() {

    private val userViewModel: UserViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentUsersBinding.inflate(inflater, container, false)
        val adapter = UsersAdapter(object : UserCallback {})
        binding.list.adapter = adapter

        userViewModel.data.observe(viewLifecycleOwner) { userModel ->
            adapter.submitList(userModel.users.filter { user ->
                userViewModel.userIds.value!!.contains(user.id)
            })
        }

        return binding.root
    }
}