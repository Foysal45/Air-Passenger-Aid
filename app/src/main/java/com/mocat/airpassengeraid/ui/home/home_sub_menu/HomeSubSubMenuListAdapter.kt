package com.mocat.airpassengeraid.ui.home.home_sub_menu

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.mocat.airpassengeraid.R
import com.mocat.airpassengeraid.api.model.home.Menu
import com.mocat.airpassengeraid.databinding.ItemHomeSubSubContentListBinding

class HomeSubSubMenuListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    private val mainSubSubContentList: MutableList<Menu> = mutableListOf()
    var onSubSubItemClick: ((mainContentList: Menu, position: Int) -> Unit)? = null

    private val options = RequestOptions()
        .placeholder(R.drawable.civil_aviation_logo)
        .error(R.drawable.civil_aviation_logo)
    private val imageUrl = "http://15.206.81.173:3008/"


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding: ItemHomeSubSubContentListBinding =
            ItemHomeSubSubContentListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewModel(binding)
    }

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        if (holder is ViewModel) {
            val model = mainSubSubContentList[position]
            val binding = holder.binding


            if (model.child.isNotEmpty()){
                binding.homeSubSubContentTitle.text = model.child.first().childCategoryDetails.first().name//Akhane lang pass korte hobe
            }


           /* if (model.media.isNotEmpty()) {
                Glide.with(binding.homeSubSubContentIcon)
                    .load(imageUrl+model.child.first().media.first().mediaInfo.source)
                    .apply(options)
                    .into(binding.homeSubSubContentIcon)
            }*/

        }
    }

    override fun getItemCount(): Int =  mainSubSubContentList.size

    inner class ViewModel(val binding: ItemHomeSubSubContentListBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                if (absoluteAdapterPosition != RecyclerView.NO_POSITION) {
                    onSubSubItemClick?.invoke(mainSubSubContentList[absoluteAdapterPosition], absoluteAdapterPosition)
                }
            }
        }


    }

    @SuppressLint("NotifyDataSetChanged")
    fun initLoad(list: List<Menu>) {
        mainSubSubContentList.clear()
        mainSubSubContentList.addAll(list)
        notifyDataSetChanged()
    }

}