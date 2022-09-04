package ru.netology.nework.viewmodel

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.netology.nework.auth.AppAuth
import ru.netology.nework.dto.Job
import ru.netology.nework.model.JobModel
import ru.netology.nework.model.JobModelState
import ru.netology.nework.repository.JobRepository
import ru.netology.nework.ui.USER_ID
import ru.netology.nework.util.SingleLiveEvent
import javax.inject.Inject

private val empty = Job(
    id = 0,
    name = "",
    position = "",
    start = "",
    finish = ""
)

@HiltViewModel
class JobViewModel @Inject constructor(
    private val jobRepository: JobRepository,
    stateHandle: SavedStateHandle,
    appAuth: AppAuth
) : ViewModel() {

    private var profileId = stateHandle.get(USER_ID) ?: appAuth.authStateFlow.value.id

    @OptIn(ExperimentalCoroutinesApi::class)
    val data: Flow<List<Job>> = appAuth.authStateFlow
        .flatMapLatest { (myId, _) ->
            jobRepository.data.map {
                JobModel()
                it.map { job ->
                    job.copy(
                        ownedByMe = profileId == myId
                    )
                }
            }
        }

    private val _dataState = MutableLiveData<JobModelState>()
    val dataState: LiveData<JobModelState>
        get() = _dataState

    private val edited = MutableLiveData(empty)

    private val _userId = MutableLiveData<Int>()
    val userId: LiveData<Int>
        get() = _userId

    private val _jobCreated = SingleLiveEvent<Unit>()
    val jobCreated: LiveData<Unit>
        get() = _jobCreated

    fun loadJobs() = viewModelScope.launch {
        try {
            _dataState.postValue(JobModelState(loading = true))
            jobRepository.getByUserId(profileId)
            _dataState.postValue(JobModelState())
        } catch (e: Exception) {
            _dataState.postValue(JobModelState(error = true))
        }
    }

    fun save() = edited.value?.let { job ->
        _jobCreated.value = Unit
        viewModelScope.launch {
            try {
                _dataState.postValue(JobModelState(loading = true))
                jobRepository.save(job)
                _dataState.postValue(JobModelState())
            } catch (e: Exception) {
                _dataState.postValue(JobModelState(error = true))
            }
        }
        edited.value = empty
    }

    fun changeJob(name: String, position: String, start: String, finish: String? ) {
        edited.value?.let {
            val nameText = name.trim()
            val positionText = position.trim()

            if (edited.value?.name != nameText) {
                edited.value = edited.value?.copy(name = nameText)
            }
            if (edited.value?.position != positionText) {
                edited.value = edited.value?.copy(position = positionText)
            }
            if (edited.value?.start != start) {
                edited.value = edited.value?.copy(start = start)
            }
            if (edited.value?.finish != finish) {
                edited.value = edited.value?.copy(finish = finish)
            }
        }
    }

    fun edit(job: Job) {
        edited.value = job
    }

    fun setId(id: Int) {
        _userId.value = id
    }
}