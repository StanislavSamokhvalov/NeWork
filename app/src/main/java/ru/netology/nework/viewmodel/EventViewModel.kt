package ru.netology.nework.viewmodel

import androidx.lifecycle.*
import androidx.lifecycle.switchMap
import androidx.paging.PagingData
import androidx.paging.map
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.netology.nework.auth.AppAuth
import ru.netology.nework.dto.Event
import ru.netology.nework.enumeration.EventType
import ru.netology.nework.model.EventModelState
import ru.netology.nework.repository.EventRepository
import ru.netology.nework.util.SingleLiveEvent
import javax.inject.Inject

private val empty = Event(
    id = 0,
    authorId = 0,
    author = "",
    authorAvatar = "",
    content = "",
    published = "2022-07-20 20:00",
    datetime = "2022-07-20 20:00",
    type = EventType.ONLINE,
    speakerIds = emptySet()
)

@HiltViewModel
class EventViewModel @Inject constructor(
    private val eventRepository: EventRepository,
    appAuth: AppAuth
) : ViewModel() {

    @OptIn(ExperimentalCoroutinesApi::class)
    val data: Flow<PagingData<Event>> = appAuth.authStateFlow
        .flatMapLatest { (myId, _) ->
            eventRepository.data.map { event ->
                event.map {
                    it.copy(
                        ownedByMe = it.authorId == myId,
                        participatedByMe = it.participantsIds.contains(myId),
                        likedByMe = it.likeOwnerIds.contains(myId)
                    )
                }
            }
        }.flowOn(Dispatchers.Default)

    val edited = MutableLiveData(empty)

    private val _dataState = MutableLiveData<EventModelState>()
    val dataState: LiveData<EventModelState>
        get() = _dataState

    private val _eventCreated = SingleLiveEvent<Unit>()
    val eventCreated: LiveData<Unit>
        get() = _eventCreated

    init {
        loadEvent()
    }

    fun loadEvent() = viewModelScope.launch {
        try {
            _dataState.postValue(EventModelState(loading = true))
            eventRepository.getAll()
            _dataState.postValue(EventModelState())
        } catch (e: Exception) {
            _dataState.postValue(EventModelState(error = true))
        }
    }

    fun save() = edited.value?.let { event ->
        _eventCreated.value = Unit
        viewModelScope.launch {
            try {
                _dataState.postValue(EventModelState(loading = true))
                eventRepository.save(event)
                _dataState.postValue(EventModelState())
            } catch (e: Exception) {
                _dataState.postValue(EventModelState(error = true))
            }
        }
        edited.value = empty
    }

    fun changeContent(content: String, date: String) {
        edited.value?.let {
            val text = content.trim()
            if (edited.value?.content != text) {
                edited.value = edited.value?.copy(content = text)
            }
            if (edited.value?.datetime != date) {
                edited.value = edited.value?.copy(datetime = date)
            }
        }
    }

    fun refreshEvents() = viewModelScope.launch {
        try {
            _dataState.postValue(EventModelState(refreshing = true))
            eventRepository.getAll()
            _dataState.postValue(EventModelState())
        } catch (e: Exception) {
            _dataState.postValue(EventModelState(error = true))
        }
    }

    fun edit(event: Event) {
        edited.value = event
    }

    fun joinById(id: Int) = viewModelScope.launch {
        try {
            eventRepository.joinById(id)
        } catch (e: Exception) {
            _dataState.postValue(EventModelState(error = true))
        }
    }

    fun unJoinById(id: Int) = viewModelScope.launch {
        try {
            eventRepository.unJoinById(id)
        } catch (e: Exception) {
            _dataState.postValue(EventModelState(error = true))
        }
    }

    fun likeById(id: Int) = viewModelScope.launch {
        try {
            eventRepository.likeById(id)
        } catch (e: Exception) {
            _dataState.postValue(EventModelState(error = true))
        }
    }

    fun unlikeById(id: Int) = viewModelScope.launch {
        try {
            eventRepository.unlikeById(id)
        } catch (e: Exception) {
            _dataState.postValue(EventModelState(error = true))
        }
    }

    fun removeById(id: Int) = viewModelScope.launch {
        try {
            eventRepository.removeById(id)
        } catch (e: Exception) {
            _dataState.postValue(EventModelState(error = true))
        }
    }
}