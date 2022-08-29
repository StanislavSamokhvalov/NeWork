package ru.netology.nework.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.netology.nework.dto.Token
import ru.netology.nework.model.AuthFormState
import ru.netology.nework.repository.AuthRepositoryImpl
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authRepository: AuthRepositoryImpl
) : ViewModel() {

    private val _data = MutableLiveData<Token>()
    val data: LiveData<Token>
        get() = _data

    private val _authForm = MutableLiveData<AuthFormState>()
    val authForm: LiveData<AuthFormState>
        get() = _authForm

    fun attemptLogin(login: String, password: String) {
        viewModelScope.launch {
            _authForm.postValue(AuthFormState(loading = true))
            try {
                _data.value = authRepository.authUser(login, password)
                _authForm.postValue(AuthFormState())
            } catch (e: IOException) {
                _authForm.postValue(AuthFormState(error = true))
            } catch (e: Exception) {
                _authForm.postValue(AuthFormState(errorAuth = true))
            }
        }
    }
}

