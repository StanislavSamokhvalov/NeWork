package ru.netology.nework.auth

import android.content.SharedPreferences
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import ru.netology.nework.api.AuthApiService
import ru.netology.nework.dto.PushToken
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppAuth @Inject constructor(
    private val api: AuthApiService,
    private val authPrefs: SharedPreferences
) {
    companion object {
        const val idKey = "id"
        const val tokenKey = "token"
    }

    private val _authStateFlow: MutableStateFlow<AuthState>

    init {
        val id = authPrefs.getInt(idKey, 0)
        val token = authPrefs.getString(tokenKey, null)

        if (id == 0 || token == null) {
            _authStateFlow = MutableStateFlow(AuthState())
            with(authPrefs.edit()) {
                clear()
                apply()
            }
        } else {
            _authStateFlow = MutableStateFlow(AuthState(id, token))
        }
        sendPushToken()
    }

    val authStateFlow: StateFlow<AuthState> = _authStateFlow.asStateFlow()

    @Synchronized
    fun setAuth(id: Int, token: String) {
        _authStateFlow.value = AuthState(id, token)
        with(authPrefs.edit()) {
            putInt(idKey, id)
            putString(tokenKey, token)
            apply()
        }
        sendPushToken()
    }

    @Synchronized
    fun removeAuth() {
        _authStateFlow.value = AuthState()
        with(authPrefs.edit()) {
            clear()
            commit()
        }
        sendPushToken()
    }

    fun sendPushToken(token: String? = null) {
        CoroutineScope(Dispatchers.Default).launch {
            try {
                val pushToken = PushToken(token ?: Firebase.messaging.token.await())
                api.savePushToken(pushToken)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

data class AuthState(val id: Int = 0, val token: String? = null)