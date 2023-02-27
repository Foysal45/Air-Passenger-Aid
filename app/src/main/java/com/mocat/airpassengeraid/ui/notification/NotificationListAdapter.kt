package com.mocat.airpassengeraid.ui.notification

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mocat.airpassengeraid.api.model.notification.getNotificationList.Notification
import com.mocat.airpassengeraid.api.model.notification.getNotificationList.NotificationPayload
import com.mocat.airpassengeraid.databinding.ItemNotificationListBinding

class NotificationListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val notificationList: MutableList<Notification> = mutableListOf()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
            ItemNotificationListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            val model = notificationList[position]
            val binding = holder.binding

            binding.notificationTitle.text = model.title
            binding.notificationDescription.text = model.body

        }
    }

    override fun getItemCount(): Int = notificationList.size

    inner class ViewHolder(val binding: ItemNotificationListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
            }
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    fun initLoad(list: NotificationPayload) {
        notificationList.clear()
        notificationList.addAll(list.notification)
        notifyDataSetChanged()
    }
}