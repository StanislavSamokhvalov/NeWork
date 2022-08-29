package ru.netology.nework.viewmodel

import android.net.Uri
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.netology.nework.auth.AppAuth
import ru.netology.nework.dto.PhotoUpload
import ru.netology.nework.dto.User
import ru.netology.nework.model.MediaModel
import ru.netology.nework.model.UserModel
import ru.netology.nework.model.UserModelState
import ru.netology.nework.repository.UserRepository
import ru.netology.nework.ui.USER_ID
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository,
    stateHandle: SavedStateHandle,
    appAuth: AppAuth
) : ViewModel() {

    val data: LiveData<UserModel> = userRepository.data
        .map { user ->
            UserModel(
                user,
                user.isEmpty()
            )
        }.asLiveData(Dispatchers.Default)

    private var profileId = stateHandle[USER_ID] ?: appAuth.authStateFlow.value.id

    private val _dataState = MutableLiveData<UserModelState>()
    val dataState: LiveData<UserModelState>
        get() = _dataState

    private val _user = MutableLiveData<User>()
    val user: LiveData<User>
        get() = _user

    private val _usersIds = MutableLiveData<Set<Int>>()
    val userIds: LiveData<Set<Int>>
        get() = _usersIds

    private val noPhoto = MediaModel()
    private val _photo = MutableLiveData(noPhoto)
    val photo: LiveData<MediaModel>
        get() = _photo

    init {
        loadUsers()
        openUser(profileId)
    }

    private fun loadUsers() = viewModelScope.launch {
        try {
            _dataState.postValue(UserModelState(loading = true))
            userRepository.getAll()
            _dataState.postValue(UserModelState())
        } catch (e: Exception) {
            _dataState.postValue(UserModelState(error = true))
        }
    }

    fun openUser(id: Int) = viewModelScope.launch {
        try {
            _dataState.postValue(UserModelState(loading = true))
            _user.value = userRepository.getUserById(id)
            _dataState.postValue(UserModelState())
        } catch (e: Exception) {
            _dataState.postValue(UserModelState(error = true))
        }
    }

    fun getUsersIds(set: Set<Int>) {
        _usersIds.value = set
    }

    fun uploadAvatar() = viewModelScope.launch {
        try {
            _dataState.postValue(UserModelState(loading = true))
            _photo.value?.uri?.let {
                PhotoUpload(it)
            }.run {
                this?.let { userRepository.uploadAvatar(it) }
            }
            _dataState.postValue(UserModelState())
        } catch (e: Exception) {
            _dataState.postValue(UserModelState(error = true))
        }
    }

    fun changePhoto(uri: Uri?) {
        _photo.value = MediaModel(uri)
    }
}