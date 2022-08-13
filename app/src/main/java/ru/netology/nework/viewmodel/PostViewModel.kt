package ru.netology.nework.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.filter
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
import ru.netology.nework.dto.MediaUpload
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
                    it.copy(
                        ownedByMe = it.authorId == myId,
                        likedByMe = it.likeOwnerIds.contains(myId)
                    )
                }.filter { validPost ->
                    !hidePosts.contains(validPost)
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

    private val _media = MutableLiveData(noMedia)
    val media: LiveData<MediaModel>
        get() = _media

    private val _edited = MutableLiveData(empty)
    val edited: LiveData<Post>
        get() = _edited

    private val hidePosts = mutableSetOf<Post>()

    init {
        loadPosts()
    }

    private fun loadPosts() = viewModelScope.launch {
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
            _postCreated.value = Unit
            viewModelScope.launch {
                _dataState.postValue(PostModelState(loading = true))
                try {
                    when (_media.value) {
                        noMedia -> postRepository.save(post)
                        else -> _media.value?.inputStream?.let { stream ->
                            postRepository.saveWithAttachment(
                                post,
                                MediaUpload(stream),
                                _media.value?.type!!
                            )
                        }
                    }
                    _dataState.postValue(PostModelState())
                } catch (e: Exception) {
                    _dataState.postValue(PostModelState(error = true))
                }
            }
            _edited.value = empty
            _media.value = noMedia
        }
    }

    fun refreshPosts() = viewModelScope.launch {
        try {
            _dataState.postValue(PostModelState(refreshing = true))
            postRepository.getAll()
            _dataState.postValue(PostModelState())
        } catch (e: Exception) {
            _dataState.postValue(PostModelState(error = true))
        }
    }

    fun changeMedia(uri: Uri?, inputStream: InputStream?, type: AttachmentType?) {
        _media.value = MediaModel(uri, inputStream, type)
    }

    fun changeContent(content: String) {
        edited.value?.let {
            val text = content.trim()
            if (it.content != text) {
                _edited.value = it.copy(content = text)
            }
        }
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

    fun hidePost(post: Post) {
        hidePosts.add(post)
        viewModelScope.launch {
            postRepository.getAll()
        }
    }

    fun edit(post: Post) {
        _edited.value = post
    }
}