package ru.netology.nework.viewmodel

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.netology.nework.model.ModelState
import ru.netology.nework.model.UserModel
import ru.netology.nework.model.UserModelState
import ru.netology.nework.repository.UserRepository
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {

    val data: LiveData<UserModel> = repository.data
        .map { user ->
            UserModel(
                user,
                user.isEmpty()
            )
        }.asLiveData()

    private val _dataState = MutableLiveData<UserModelState>()
    val dataState: LiveData<UserModelState>
        get() = _dataState

    init {
        loadUsers()
    }

    fun loadUsers() = viewModelScope.launch {
        try {
            _dataState.postValue(UserModelState(loading = true))
            repository.getAll()
            _dataState.postValue(UserModelState())
        } catch (e: Exception) {
            _dataState.postValue(UserModelState(error = true))
        }
    }
}