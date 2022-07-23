package ru.netology.nework.error

import java.io.IOException
import java.lang.RuntimeException
import java.sql.SQLException

sealed class AppError(var code: String) : RuntimeException() {
    companion object {
        fun from(e: Throwable): AppError = when (e) {
            is AppError -> e
            is SQLException -> DbError
            is IOException -> NetworkError
            else -> UnknownError
        }
    }
}

class ApiError(val status: Int, code: String) : AppError(code)
object DbError : AppError("db_error")
object NetworkError : AppError("network_error")
object UnknownError : AppError("unknown_error")