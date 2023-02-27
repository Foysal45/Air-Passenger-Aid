package com.mocat.airpassengeraid.ui.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.mocat.airpassengeraid.R
import com.mocat.airpassengeraid.api.model.home.Menu
import com.mocat.airpassengeraid.databinding.ItemHomeContentListBinding

class HomeContentListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    private val mainContentList: MutableList<Menu> = mutableListOf()
    var onItemClick: ((mainContentList: Menu, position: Int) -> Unit)? = null

    private val options = RequestOptions()
        .placeholder(R.drawable.civil_aviation_logo)
        .error(R.drawable.civil_aviation_logo)
    private val imageUrl = "http://15.206.81.173:3008/"


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding: ItemHomeContentListBinding =
            ItemHomeContentListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewModel(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewModel) {
            val model = mainContentList[position]
            val binding = holder.binding


            if (model.categoryDetails.isNotEmpty()){
                binding.contentTitle.text = model.categoryDetails.first().name
            }

            if (model.media.isNotEmpty()) {
                Glide.with(binding.contentIcon)
                    .load(imageUrl+model.media.first().mediaInfo.source)
                    .apply(options)
                    .into(binding.contentIcon)
            }

        }
    }

    override fun getItemCount(): Int =  mainContentList.size

    inner class ViewModel(val binding: ItemHomeContentListBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                if (absoluteAdapterPosition != RecyclerView.NO_POSITION) {
                    onItemClick?.invoke(mainContentList[absoluteAdapterPosition], absoluteAdapterPosition)
                }
            }
        }


    }

    @SuppressLint("NotifyDataSetChanged")
    fun initLoad(list: List<Menu>) {
        mainContentList.clear()
        mainContentList.addAll(list)
        notifyDataSetChanged()
    }
}