package ru.netology.nework.ui

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.github.dhaval2404.imagepicker.ImagePicker
import com.github.dhaval2404.imagepicker.constant.ImageProvider
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.nework.R
import ru.netology.nework.auth.AppAuth
import ru.netology.nework.databinding.FragmentSingUpBinding
import ru.netology.nework.viewmodel.AuthViewModel
import ru.netology.nework.viewmodel.SignUpViewModel
import javax.inject.Inject

@AndroidEntryPoint
class SingUpFragment : Fragment() {
    @Inject
    lateinit var appAuth: AppAuth

    private val signUpViewModel: SignUpViewModel by viewModels()
    private val authViewModel: AuthViewModel by viewModels()

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

        val pickPhotoLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                when (it.resultCode) {
                    ImagePicker.RESULT_ERROR -> {
                        Snackbar.make(
                            binding.root,
                            ImagePicker.getError(it.data),
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                    Activity.RESULT_OK -> {
                        val uri: Uri? = it.data?.data
                        signUpViewModel.changePhoto(uri)
                    }
                }
            }


        signUpViewModel.data.observe(viewLifecycleOwner) {
            appAuth.setAuth(it.id, it.token)
            findNavController().navigateUp()
        }

        signUpViewModel.authForm.observe(viewLifecycleOwner) { state ->
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
            avatarButton.setOnClickListener {
                ImagePicker.with(this@SingUpFragment)
                    .cropSquare()
                    .compress(2048)
                    .provider(ImageProvider.GALLERY)
                    .createIntent(pickPhotoLauncher::launch)
            }

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