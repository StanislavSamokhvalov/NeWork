package ru.netology.nework.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nework.R
import ru.netology.nework.databinding.CardEventBinding
import ru.netology.nework.dto.Event
import ru.netology.nework.util.AndroidUtils.uploadingAvatar


interface EventCallback {
    fun onLike(event: Event) {}
    fun onEdit(event: Event) {}
    fun onRemove(event: Event) {}
    fun onShare(event: Event) {}
    fun onJoin(event: Event) {}
    fun onParty(event: Event) {}
    fun onSpeakers(event: Event) {}
    fun onLikeOwners(event: Event) {}
    fun onAudio(event: Event) {}
    fun onVideo(event: Event) {}
    fun onOpenAvatar(event: Event) {}
}

class EventAdapter(
    private val eventCallback: EventCallback,
) : PagingDataAdapter<Event, EventViewHolder>(EventDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding = CardEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventViewHolder(binding, eventCallback)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = getItem(position)
        event?.let { holder.bind(it) }
    }
}

class EventViewHolder(
    private val binding: CardEventBinding,
    private val eventCallback: EventCallback,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(event: Event) {
        with(binding) {
            uploadingAvatar(userAvatar, event.authorAvatar)
            author.text = event.author
            published.text = event.published
            content.text = event.content
            eventDateEdit.text = event.datetime
            eventFormatEdit.text = event.type.toString()
            like.isChecked = event.likedByMe

            userAvatar.setOnClickListener {
                eventCallback.onOpenAvatar(event)
            }

            speakers.setOnClickListener {
                eventCallback.onSpeakers(event)
            }

            participants.setOnClickListener {
                eventCallback.onParty(event)
            }

            join.setOnClickListener {
                if (join.isChecked) join.setText(R.string.join_button_checked) else join.setText(R.string.join_button_unchecked)
                eventCallback.onJoin(event)
            }

            like.setOnClickListener {
                eventCallback.onLike(event)
            }

            share.setOnClickListener {
                eventCallback.onShare(event)
            }

            menu.visibility = if (event.ownedByMe) View.VISIBLE else View.INVISIBLE
            menu.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.options_post)
                    menu.setGroupVisible(R.id.owned, event.ownedByMe)
                    setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.remove -> {
                                eventCallback.onRemove(event)
                                true
                            }
                            R.id.edit -> {
                                eventCallback.onEdit(event)
                                true
                            }
                            else -> false
                        }
                    }
                }.show()
            }
        }
    }

}

class EventDiffCallback : DiffUtil.ItemCallback<Event>() {
    override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean {
        return oldItem == newItem
    }
}