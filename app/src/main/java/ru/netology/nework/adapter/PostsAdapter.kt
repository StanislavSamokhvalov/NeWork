package ru.netology.nework.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.netology.nework.R
import ru.netology.nework.databinding.CardPostBinding
import ru.netology.nework.dto.Post
import ru.netology.nework.enumeration.AttachmentType
import ru.netology.nework.util.AndroidUtils.uploadingAvatar

interface PostCallback {
    fun onLike(post: Post) {}
    fun onEdit(post: Post) {}
    fun onHide(post: Post) {}
    fun onRemove(post: Post) {}
    fun onShare(post: Post) {}
    fun onVideo(post: Post) {}
    fun onAudio(post: Post) {}
    fun onOpenImage(post: Post) {}
    fun onOpenAvatar(post: Post) {}
}

class PostsAdapter(
    private val postCallback: PostCallback,
) : PagingDataAdapter<Post, PostViewHolder>(PostDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = CardPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding, postCallback)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val event = getItem(position)
        event?.let { holder.bind(it) }
    }
}

class PostViewHolder(
    private val binding: CardPostBinding,
    private val postCallback: PostCallback,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(post: Post) {
        with(binding) {
            uploadingAvatar(userAvatar, post.authorAvatar)
            author.text = post.author
            published.text = post.published
            content.text = post.content
            like.isChecked = post.likedByMe

            imageView.isVisible = post.attachment?.type == AttachmentType.IMAGE
            groupMedia.isVisible = post.attachment?.type == AttachmentType.VIDEO
            groupAudio.isVisible = post.attachment?.type == AttachmentType.AUDIO

            post.attachment?.apply {
                Glide.with(imageView)
                    .load(this.url)
                    .placeholder(R.drawable.ic_image_placeholder_150dp)
                    .error(R.drawable.ic_error_placeholder_150dp)
                    .timeout(10_000)
                    .into(imageView)
            }

            userAvatar.setOnClickListener {
                postCallback.onOpenAvatar(post)
            }

            imageView.setOnClickListener {
                postCallback.onOpenImage(post)
            }

            playVideo.setOnClickListener {
                postCallback.onVideo(post)
            }

            playAudio.setOnClickListener {
                postCallback.onAudio(post)
            }

            like.setOnClickListener {
                postCallback.onLike(post)
            }

            share.setOnClickListener {
                postCallback.onShare(post)
            }

            hide.setOnClickListener {
                postCallback.onHide(post)
            }

            menu.visibility = if (post.ownedByMe) View.VISIBLE else View.INVISIBLE

            menu.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.options_post)
                    menu.setGroupVisible(R.id.owned, post.ownedByMe)
                    setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.remove -> {
                                postCallback.onRemove(post)
                                true
                            }
                            R.id.edit -> {
                                postCallback.onEdit(post)
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

class PostDiffCallback : DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem == newItem
    }
}
