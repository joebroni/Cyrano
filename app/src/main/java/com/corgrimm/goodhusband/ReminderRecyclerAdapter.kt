package com.corgrimm.goodhusband

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.corgrimm.goodhusband.models.Reminder

class ReminderRecyclerAdapter : ListAdapter<Reminder, ReminderRecyclerAdapter.ReminderViewHolder>(ReminderssComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReminderViewHolder {
        return ReminderViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ReminderViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
    }

    class ReminderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameView: TextView = itemView.findViewById(R.id.nameView)
        private val dateView: TextView = itemView.findViewById(R.id.dateView)

        fun bind(reminder: Reminder?) {
            nameView.text = reminder!!.name
            dateView.text = reminder!!.date //TODO This needs a formatter
        }

        companion object {
            fun create(parent: ViewGroup): ReminderViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.reminder_recycler_item, parent, false)
                return ReminderViewHolder(view)
            }
        }
    }

    class ReminderssComparator : DiffUtil.ItemCallback<Reminder>() {
        override fun areItemsTheSame(oldItem: Reminder, newItem: Reminder): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Reminder, newItem: Reminder): Boolean {
            return oldItem.id == newItem.id
        }
    }
}