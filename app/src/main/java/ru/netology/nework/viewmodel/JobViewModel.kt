package ru.netology.nework.viewmodel

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.netology.nework.auth.AppAuth
import ru.netology.nework.model.JobModel
import ru.netology.nework.model.JobModelState
import ru.netology.nework.repository.JobRepository
import ru.netology.nework.ui.USER_ID
import javax.inject.Inject

@HiltViewModel
class JobViewModel @Inject constructor(
    private val jobRepository: JobRepository,
    stateHandle: SavedStateHandle,
    appAuth: AppAuth
) : ViewModel() {

    private var profileId = stateHandle.get(USER_ID) ?: appAuth.authStateFlow.value.id

    val data: LiveData<JobModel> = jobRepository.data
        .map { job ->
            JobModel(
                job,
                job.isEmpty()
            )
        }.asLiveData(Dispatchers.Default)

    private val _dataState = MutableLiveData<JobModelState>()
    val dataState: LiveData<JobModelState>
        get() = _dataState

    private val _userId = MutableLiveData<Int>()
    val userId: LiveData<Int>
        get() = _userId

    init {
        loadJobs(profileId)
    }

    fun loadJobs(id: Int) = viewModelScope.launch {
        try {
            _dataState.postValue(JobModelState(loading = true))
            jobRepository.getByUserId(id)
            _dataState.postValue(JobModelState())
        } catch (e: Exception) {
            _dataState.postValue(JobModelState(error = true))
        }
    }


    fun setId(id: Int) {
        _userId.value = id
    }
}