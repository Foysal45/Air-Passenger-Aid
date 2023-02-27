package com.mocat.airpassengeraid.ui.home.home_sub_menu

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.mocat.airpassengeraid.R
import com.mocat.airpassengeraid.api.model.home.Menu
import com.mocat.airpassengeraid.databinding.ItemHomeContentListBinding
import com.mocat.airpassengeraid.utils.logException

class HomeSubMenuListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    private val mainSubContentList: MutableList<Menu> = mutableListOf()
    var onItemClick: ((mainContentList: Menu, position: Int) -> Unit)? = null
    var onSubSubItemClick: ((mainContentList: Menu, position: Int) -> Unit)? = null
    private var expandedPosition = -1
    private var homeSubSubMenuAdapter = HomeSubSubMenuListAdapter()

    private val options = RequestOptions()
        .placeholder(R.drawable.civil_aviation_logo)
        .error(R.drawable.civil_aviation_logo)
    private val imageUrl = "http://15.206.81.173:3008/"


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding: ItemHomeContentListBinding =
            ItemHomeContentListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewModel(binding)
    }

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        if (holder is ViewModel) {
            val model = mainSubContentList[position]
            val binding = holder.binding


            if (model.categoryDetails.isNotEmpty()){
                binding.contentTitle.text = model.categoryDetails.first().name
            }

            if (model.child.isNotEmpty()){
                binding.expandBtn.visibility=View.VISIBLE
            }else{
                binding.expandBtn.visibility=View.GONE
            }

            //expandable icon action
            binding.expandBtn.setOnClickListener {

                if (position != expandedPosition) {
                    expandedPosition = position
                    val currentRotation = holder.binding.expandBtn.rotation
                    rotateView(holder.binding.expandBtn, currentRotation, 180f)
                    holder.binding.recyclerViewSubSubMenu.visibility = View.VISIBLE
                    notifyDataSetChanged()
                } else {
                    expandedPosition = -1
                    val currentRotation = holder.binding.expandBtn.rotation
                    if (currentRotation != 0f) {
                        rotateView(holder.binding.expandBtn, currentRotation, 0f)
                    }
                    holder.binding.recyclerViewSubSubMenu.visibility = View.GONE
                    notifyDataSetChanged()

                }
            }

            homeSubSubMenuAdapter.initLoad(mainSubContentList)
            with(holder.binding.recyclerViewSubSubMenu) {
                setHasFixedSize(false)
                isNestedScrollingEnabled = false
                layoutManager = LinearLayoutManager(this.context)
                adapter = homeSubSubMenuAdapter
            }

            homeSubSubMenuAdapter.onSubSubItemClick= { model, position->
                onSubSubItemClick?.invoke(model,position)
            }


            if (model.media.isNotEmpty()) {
                Glide.with(binding.contentIcon)
                    .load(imageUrl+model.media.first().mediaInfo.source)
                    .apply(options)
                    .into(binding.contentIcon)
            }

        }

    }

    override fun getItemCount(): Int =  mainSubContentList.size

    inner class ViewModel(val binding: ItemHomeContentListBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                if (absoluteAdapterPosition != RecyclerView.NO_POSITION) {
                    onItemClick?.invoke(mainSubContentList[absoluteAdapterPosition], absoluteAdapterPosition)
                }
            }
        }


    }

    private fun rotateView(view: View, start: Float = 0f, end: Float = 0f, duration: Long = 200L) {
        val rotate = ObjectAnimator.ofFloat(view, "rotation", start, end)
        rotate.duration = duration
        rotate.start()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun initLoad(list: List<Menu>) {
        mainSubContentList.clear()
        mainSubContentList.addAll(list)
        notifyDataSetChanged()
    }
}