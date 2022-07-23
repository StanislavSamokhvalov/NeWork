package ru.netology.nework.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.netology.nework.dto.Token
import ru.netology.nework.model.ModelState
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

    private val _dataState = MutableLiveData<ModelState>() // в ошибку
    val dataState: LiveData<ModelState>
        get() = _dataState

    fun attemptLogin(login: String, password: String) {
        viewModelScope.launch {
            _dataState.postValue(ModelState(loading = true))
            try {
                _data.value = authRepository.authUser(login, password)
                _dataState.postValue(ModelState())
            } catch (e: IOException) {
                _dataState.postValue(ModelState(error = true))
            } catch (e: Exception) {
                _dataState.postValue(ModelState(errorLogin = true))
            }
        }
    }
}

