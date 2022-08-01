package ru.netology.nework.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.netology.nework.dto.Token
import ru.netology.nework.model.PostModelState
import ru.netology.nework.repository.AuthRepositoryImpl
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authRepository: AuthRepositoryImpl,
) : ViewModel() {

    private val _data = MutableLiveData<Token>()
    val data: LiveData<Token>
        get() = _data

    private val _dataState = MutableLiveData<PostModelState>() // в ошибку
    val dataState: LiveData<PostModelState>
        get() = _dataState

    fun registerUser(login: String, password: String, name: String) {
        viewModelScope.launch {
            _dataState.postValue(PostModelState(loading = true))
            try {
                val token = authRepository.registerUser(login, password, name)
                _data.value = Token(token.id, token.token)
                _dataState.postValue(PostModelState())
            } catch (e: IOException) {
                _dataState.postValue(PostModelState(error = true))
            } catch (e: Exception) {
                _dataState.postValue(PostModelState(errorRegistration = true))
            }
        }
    }
}