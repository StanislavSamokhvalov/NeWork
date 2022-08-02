package ru.netology.nework.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.map
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
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
): ViewModel() {

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
}