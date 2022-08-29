package ru.netology.nework.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.netology.nework.dto.PhotoUpload
import ru.netology.nework.dto.Token
import ru.netology.nework.model.AuthFormState
import ru.netology.nework.model.MediaModel
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

    private val noPhoto = MediaModel()
    private val _photo = MutableLiveData(noPhoto)
    val photo: LiveData<MediaModel>
        get() = _photo

    private val _authForm = MutableLiveData<AuthFormState>() // в ошибку
    val authForm: LiveData<AuthFormState>
        get() = _authForm

    fun registerUser(login: String, password: String, name: String) {
        viewModelScope.launch {
            _authForm.postValue(AuthFormState(loading = true))
            try {
                val user = _photo.value?.uri?.let {
                    PhotoUpload(it)
                }.run {
                    if (this == null) authRepository.registerUser(login, password, name)
                    else authRepository.registerUserWithAvatar(login, password, name, this)
                }
                _data.value = user
                _authForm.postValue(AuthFormState())
            } catch (e: IOException) {
                _authForm.postValue(AuthFormState(error = true))
            } catch (e: Exception) {
                _authForm.postValue(AuthFormState(errorAuth = true))
            }
        }
    }

    fun changePhoto(uri: Uri?) {
        _photo.value = MediaModel(uri)
    }
}