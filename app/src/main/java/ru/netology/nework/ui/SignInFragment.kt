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
import ru.netology.nework.databinding.FragmentSignInBinding
import ru.netology.nework.viewmodel.SignInViewModel
import javax.inject.Inject

@AndroidEntryPoint
class SignInFragment : Fragment() {
    @Inject
    lateinit var appAuth: AppAuth

    private val signInViewModel: SignInViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentSignInBinding.inflate(
            inflater,
            container,
            false
        )

        signInViewModel.authForm.observe(viewLifecycleOwner) { state ->
            if (state.errorAuth) {
                binding.password.error = getString(R.string.error_auth)
            }
        }

        signInViewModel.data.observe(viewLifecycleOwner) {
            appAuth.setAuth(it.id, it.token)
            findNavController().navigateUp()
        }

        with(binding) {
            login.requestFocus()
            signInButton.setOnClickListener {
                signInViewModel.attemptLogin(
                    login.text.toString(),
                    password.text.toString()
                )
            }

            registrationButton.setOnClickListener {
                findNavController().navigate(R.id.action_signInFragment_to_singUpFragment)
            }
        }
        return binding.root
    }
}
