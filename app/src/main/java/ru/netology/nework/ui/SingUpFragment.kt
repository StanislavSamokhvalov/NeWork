package ru.netology.nework.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.nework.R
import ru.netology.nework.auth.AppAuth
import ru.netology.nework.databinding.FragmentSingUpBinding
import ru.netology.nework.viewmodel.SignUpViewModel
import javax.inject.Inject

@AndroidEntryPoint
class SingUpFragment : Fragment() {
    @Inject
    lateinit var appAuth: AppAuth

    val signUpViewModel: SignUpViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentSingUpBinding.inflate(
            inflater,
            container,
            false
        )

        signUpViewModel.data.observe(viewLifecycleOwner) {
            appAuth.setAuth(it.id, it.token)
            findNavController().navigateUp()
        }

        signUpViewModel.dataState.observe(viewLifecycleOwner) { state ->
            if (state.errorRegistration)
                Snackbar.make(
                    binding.root, R.string.error_loading,
                    BaseTransientBottomBar.LENGTH_INDEFINITE
                )
                    .setAction(R.string.retry_loading) {
                        signUpViewModel.registerUser(
                            binding.login.text.toString(),
                            binding.password.text.toString(),
                            binding.name.text.toString()
                        )
                    }.show()
        }

        with(binding) {
            signUpButton.setOnClickListener {
                if (password.text.toString() == repeatPassword.text.toString()) {
                    signUpViewModel.registerUser(
                        login.text.toString(),
                        password.text.toString(),
                        name.text.toString()
                    )
                } else {
                    repeatPassword.error = getString(R.string.error_password)
                }
            }
        }
        return binding.root
    }
}