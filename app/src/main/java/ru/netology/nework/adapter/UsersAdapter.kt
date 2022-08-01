package ru.netology.nework.adapter

import ru.netology.nework.databinding.CardUserBinding
import ru.netology.nework.dto.User
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nework.util.uploadingAvatar


interface UserCallback {
    fun onUser(user: User){}
}

class UsersAdapter(
    private val userCallback: UserCallback,
) : ListAdapter<User, UserViewHolder>(UserDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = CardUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding, userCallback)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user)
    }
}

class UserViewHolder(
    private val binding: CardUserBinding,
    private val userCallback: UserCallback,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(user: User) {
        with(binding){
            userName.text = user.name
            uploadingAvatar(userAvatar, user.avatar)
            viewUser.setOnClickListener {
                userCallback.onUser(user)
            }
        }

    }
}


class UserDiffCallback : DiffUtil.ItemCallback<User>() {
    override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem == newItem
    }
}
