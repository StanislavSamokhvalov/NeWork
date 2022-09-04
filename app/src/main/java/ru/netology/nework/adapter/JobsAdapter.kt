package ru.netology.nework.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nework.R
import ru.netology.nework.databinding.CardJobBinding
import ru.netology.nework.dto.Job
import ru.netology.nework.util.AndroidUtils.formatToDate


interface JobCallback {
    fun onEdit(job: Job) {}
    fun onRemove(job: Job) {}
}

class JobsAdapter(
    private val jobCallback: JobCallback,
) : ListAdapter<Job, JobViewHolder>(JobDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobViewHolder {
        val binding = CardJobBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return JobViewHolder(binding, jobCallback)
    }

    override fun onBindViewHolder(holder: JobViewHolder, position: Int) {
        val job = getItem(position)
        holder.bind(job)
    }
}

class JobViewHolder(
    private val binding: CardJobBinding,
    private val jobCallback: JobCallback,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(job: Job) {
        with(binding) {
            nameCompany.text = job.name
            vacancy.text = job.position
            workStart.text = formatToDate(job.start)
            workFinish.text = formatToDate(job.finish)

            menu.visibility = if (job.ownedByMe) View.VISIBLE else View.INVISIBLE
            menu.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.options_post)
                    menu.setGroupVisible(R.id.owned, job.ownedByMe)
                    setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.remove -> {
                                jobCallback.onRemove(job)
                                true
                            }
                            R.id.edit -> {
                                jobCallback.onEdit(job)
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

class JobDiffCallback : DiffUtil.ItemCallback<Job>() {
    override fun areItemsTheSame(oldItem: Job, newItem: Job): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Job, newItem: Job): Boolean {
        return oldItem == newItem
    }
}
