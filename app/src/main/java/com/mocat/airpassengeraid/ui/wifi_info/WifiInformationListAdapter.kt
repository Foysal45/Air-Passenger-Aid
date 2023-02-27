package com.mocat.airpassengeraid.ui.wifi_info

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mocat.airpassengeraid.api.model.wifi_info.Wify
import com.mocat.airpassengeraid.databinding.ItemWifiInformationListBinding

class WifiInformationListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val wifiInfoList: MutableList<Wify> = mutableListOf()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(ItemWifiInformationListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            val model = wifiInfoList[position]
            val binding = holder.binding

            binding.wifiSsidNumber.text =  model.wifiDetails.first().ssid
            binding.wifiNameValue.text = model.wifiDetails.first().name
            binding.wifiPasswordValue.text = model.wifiDetails.first().password

        }
    }

    override fun getItemCount(): Int =  wifiInfoList.size

    inner class ViewHolder(val binding: ItemWifiInformationListBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
            }
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    fun initLoad(list: List<Wify>) {
        wifiInfoList.clear()
        wifiInfoList.addAll(list)
        notifyDataSetChanged()
    }
}