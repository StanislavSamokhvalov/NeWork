package ru.netology.nework.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.nework.adapter.UserCallback
import ru.netology.nework.adapter.UsersAdapter
import ru.netology.nework.databinding.FragmentUsersBinding
import ru.netology.nework.viewmodel.UserViewModel

@AndroidEntryPoint
class UsersFragment : Fragment() {

    private val viewModel: UserViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentUsersBinding.inflate(inflater, container, false)
        val adapter = UsersAdapter(object : UserCallback {
            override fun onUser() {
                super.onUser()
            }
        })

        binding.list.adapter = adapter

        viewModel.data.observe(viewLifecycleOwner) { state ->
            adapter.submitList(state.users)
        }

        return binding.root
    }
}