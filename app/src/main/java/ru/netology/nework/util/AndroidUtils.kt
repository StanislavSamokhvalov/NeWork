package ru.netology.nework.util

import android.content.Context
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import com.bumptech.glide.Glide
import ru.netology.nework.R
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

object AndroidUtils {
    fun hideKeyboard(view: View) {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun uploadingAvatar(view: ImageView, avatar: String?) {
        Glide.with(view)
            .load(avatar)
            .circleCrop()
            .placeholder(R.drawable.ic_face_24dp)
            .timeout(10_000)
            .into(view)
    }

    fun uploadingAvatarBackground (view: ImageView, avatar: String?) {
        Glide.with(view)
            .load(avatar)
            .placeholder(R.drawable.ic_face_24dp)
            .timeout(10_000)
            .into(view)
    }

    fun formatDateTime(value: String): String {
        return if (value != " ") {
            val date = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).parse(value)
            val formatter = DateTimeFormatter.ISO_INSTANT
0
            formatter.format(date?.toInstant())
        } else "2021-08-17T16:46:58.887547Z"
    }

    fun formatToDate(value: String?): String {
        val formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
            .withLocale(Locale.ROOT)
            .withZone(ZoneId.systemDefault())

        return formatter.format(Instant.parse(value))
    }

}

