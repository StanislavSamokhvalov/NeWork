package ru.netology.nework.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.nework.R
import ru.netology.nework.adapter.UserCallback
import ru.netology.nework.adapter.UsersAdapter
import ru.netology.nework.databinding.FragmentUsersBinding
import ru.netology.nework.dto.User
import ru.netology.nework.viewmodel.UserViewModel

const val USER_ID = "USER_ID"

@AndroidEntryPoint
class UsersFragment : Fragment() {

    private val userViewModel: UserViewModel by viewModels()
    private val bundle = Bundle()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentUsersBinding.inflate(inflater, container, false)
        val adapter = UsersAdapter(object : UserCallback {
            override fun onUser(user: User) {
                userViewModel.openUser(user.id)
                bundle.putInt(USER_ID, user.id)
                bundle.putString("url", user.avatar)
                bundle.putString("name", user.name)
                findNavController().navigate(R.id.userProfileFragment, bundle)
            }
        })

        binding.list.adapter = adapter

        binding.swiperefresh.setOnRefreshListener {
            userViewModel.refreshUsers()
        }

        userViewModel.dataState.observe(viewLifecycleOwner) { state ->
            binding.swiperefresh.isRefreshing = state.refreshing
            if (state.error) {
                Snackbar.make(binding.root, R.string.error_loading, Snackbar.LENGTH_LONG).show()
            }
        }

        userViewModel.data.observe(viewLifecycleOwner) { state ->
            adapter.submitList(state.users)
        }

        return binding.root
    }
}