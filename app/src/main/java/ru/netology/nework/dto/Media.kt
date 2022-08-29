package ru.netology.nework.dto

import android.net.Uri
import java.io.File
import java.io.InputStream

data class Media(val url: String)

data class MediaUpload(val inputStream: InputStream)

data class PhotoUpload(val uri: Uri)