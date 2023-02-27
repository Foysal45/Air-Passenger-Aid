package com.mocat.airpassengeraid.ui.search

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.mocat.airpassengeraid.R
import com.mocat.airpassengeraid.api.model.home.Menu
import com.mocat.airpassengeraid.api.model.home.details.Section
import com.mocat.airpassengeraid.api.model.notification.getNotificationList.Notification
import com.mocat.airpassengeraid.api.model.notification.getNotificationList.NotificationPayload
import com.mocat.airpassengeraid.databinding.ItemHomeContentListBinding
import com.mocat.airpassengeraid.databinding.ItemNotificationListBinding
import com.mocat.airpassengeraid.databinding.ItemSearchContactListBinding
import com.mocat.airpassengeraid.databinding.ItemViewSpinnerItemBinding

class SearchListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val menuList: MutableList<Section> = mutableListOf()
    var onItemClick: ((menuList: Section, position: Int) -> Unit)? = null
    private val options = RequestOptions()
        .placeholder(R.drawable.civil_aviation_logo)
        .error(R.drawable.civil_aviation_logo)
    private val imageUrl = "http://15.206.81.173:3008/"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(ItemSearchContactListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            val model = menuList[position]
            val binding = holder.binding

            binding.contentTitle.text = model.categoryInfo.detail.name

            if (model.media.isNotEmpty()) {
                Glide.with(binding.contentIcon)
                    .load(imageUrl+model.media.first().mediaInfo.source)
                    .apply(options)
                    .into(binding.contentIcon)
            }

        }
    }

    override fun getItemCount(): Int = menuList.size

    inner class ViewHolder(val binding: ItemSearchContactListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                if (absoluteAdapterPosition != RecyclerView.NO_POSITION) {
                    onItemClick?.invoke(menuList[absoluteAdapterPosition], absoluteAdapterPosition)
                }
            }
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    fun initLoad(list: List<Section>) {
        menuList.clear()
        menuList.addAll(list)
        notifyDataSetChanged()
    }
}