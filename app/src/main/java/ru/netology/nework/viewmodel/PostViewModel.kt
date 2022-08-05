package ru.netology.nework.viewmodel

import android.net.Uri
import androidx.lifecycle.*
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
import ru.netology.nework.dto.Post
import ru.netology.nework.enumeration.AttachmentType
import ru.netology.nework.model.MediaModel
import ru.netology.nework.model.PostModelState
import ru.netology.nework.repository.PostRepository
import ru.netology.nework.util.SingleLiveEvent
import java.io.InputStream
import javax.inject.Inject

private val empty = Post(
    0,
    0,
    "",
    "",
    "",
    ""
)

@HiltViewModel
@OptIn(ExperimentalCoroutinesApi::class)
class PostViewModel @Inject constructor(
    private val postRepository: PostRepository,
    appAuth: AppAuth
) : ViewModel() {

    val data: Flow<PagingData<Post>> = appAuth
        .authStateFlow
        .flatMapLatest { (myId, _) ->
            postRepository.data.map { posts ->
                posts.map {
                    it.copy(ownedByMe = it.authorId == myId)
                }
            }
        }.flowOn(Dispatchers.Default)

private val _dataState = MutableLiveData<PostModelState>()
val dataState: LiveData<PostModelState>
    get() = _dataState

private val _postCreated = SingleLiveEvent<Unit>()
val postCreated: LiveData<Unit>
    get() = _postCreated

private val noMedia = MediaModel()
val _media = MutableLiveData(noMedia)
val media: LiveData<MediaModel>
    get() = _media

val edited = MutableLiveData(empty)

init {
    loadPosts()
}

fun loadPosts() = viewModelScope.launch {
    try {
        _dataState.postValue(PostModelState(loading = true))
        postRepository.getAll()
        _dataState.postValue(PostModelState())
    } catch (e: Exception) {
        _dataState.postValue(PostModelState(error = true))
    }
}

fun save() {
    edited.value?.let { post ->
        viewModelScope.launch {
            _dataState.postValue(PostModelState(loading = true))
            try {
                when (_media.value) {
                    noMedia -> postRepository.save(post)
//                        else -> _media.value?.inputStream?.let {
//                            MediaUpload(it)
//                        }?.let {
//                            repository.saveWithAttachment(post, it, _media.value?.type!!)
//                        }
                }
                _dataState.value = PostModelState()
                _postCreated.value = Unit
            } catch (e: Exception) {
                _dataState.value = PostModelState(error = true)
            }
        }
        edited.value = empty
        _media.value = noMedia
    }
}

fun changeContent(content: String) {
    val text = content.trim()
    if (edited.value?.content == text) {
        return
    }
    edited.value = edited.value?.copy(content = text)
}

fun changeMedia(uri: Uri?, inputStream: InputStream, type: AttachmentType) {
    _media.value = MediaModel(uri, inputStream, type)
}

fun likeById(id: Int) = viewModelScope.launch {
    try {
        postRepository.likeById(id)
    } catch (e: Exception) {
        _dataState.postValue(PostModelState(error = true))
    }
}

fun unlikeById(id: Int) = viewModelScope.launch {
    try {
        postRepository.unlikeById(id)
    } catch (e: Exception) {
        _dataState.postValue(PostModelState(error = true))
    }
}

fun removeById(id: Int) = viewModelScope.launch {
    try {
        postRepository.removeById(id)
    } catch (e: Exception) {
        _dataState.postValue(PostModelState(error = true))
    }
}

fun edit(post: Post) {
    edited.value = post
}
}