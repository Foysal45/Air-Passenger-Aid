package com.mocat.airpassengeraid.ui.favourite

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.mocat.airpassengeraid.R
import com.mocat.airpassengeraid.api.model.favourite.Wish
import com.mocat.airpassengeraid.databinding.ItemFavouriteContentListBinding

class FavouriteContentListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    private val articleList: MutableList<Wish> = mutableListOf()
    var onItemClick: ((articleList: Wish, position: Int) -> Unit)? = null

    private val options = RequestOptions()
        .placeholder(R.drawable.civil_aviation_logo)
        .error(R.drawable.civil_aviation_logo)
    private val imageUrl = "http://15.206.81.173:3008/"


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding: ItemFavouriteContentListBinding =
            ItemFavouriteContentListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewModel(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewModel) {
            val model = articleList[position]
            val binding = holder.binding


            if (model.articleInfo.sections.first().details.isNotEmpty()) {
                binding.favouriteContentTitle.text = model.articleInfo.sections.first().details.first().title
                binding.favouriteContentDetails.text = model.articleInfo.sections.first().details.first().description
            }

                if (model.articleInfo.media.isNotEmpty()) {
                    Glide.with(binding.favouriteContentImage)
                        .load(imageUrl + model.articleInfo.media.first().mediaInfo.source)
                        .apply(options)
                        .into(binding.favouriteContentImage)
                }

           /* binding.repoId.text = "Repo Id : ${model.id}"
            binding.repoName.text = "Repo Name : ${model.name}"
            binding.repoFullName.text = "Repo Full Name : ${model.full_name}"*/

        }
    }

    override fun getItemCount(): Int = articleList.size

    inner class ViewModel(val binding: ItemFavouriteContentListBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                if (absoluteAdapterPosition != RecyclerView.NO_POSITION) {
                    onItemClick?.invoke(articleList[absoluteAdapterPosition], absoluteAdapterPosition)
                }
            }
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    fun initLoad(list: List<Wish>) {
        articleList.clear()
        articleList.addAll(list)
        notifyDataSetChanged()
    }
}