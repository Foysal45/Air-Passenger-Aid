package com.mocat.airpassengeraid.ui.contact_information

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mocat.airpassengeraid.api.model.contact_information.Contact
import com.mocat.airpassengeraid.databinding.ItemImportantContactListBinding

class ImportantContactListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    private val importantContactList: MutableList<Contact> = mutableListOf()
    var onMobileNumberClick: ((repoListClick: Contact, position: Int) -> Unit)? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding: ItemImportantContactListBinding =
            ItemImportantContactListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewModel(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewModel) {
            val model = importantContactList[position]
            val binding = holder.binding

            if (model.contactDetails.isNotEmpty()) {
                binding.nameLiablePerson.text = model.contactDetails.first().name
                binding.mobileLiablePerson.text = model.contactDetails.first().phonenumber
                binding.telephoneNumberLiablePerson.text = model.contactDetails.first().telephone
                binding.liablePersonEmail.text = model.contactDetails.first().email
            }


        }
    }

    override fun getItemCount(): Int =  importantContactList.size

    inner class ViewModel(val binding: ItemImportantContactListBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.mobileNumberLayout.setOnClickListener {
                if (absoluteAdapterPosition != RecyclerView.NO_POSITION) {
                    onMobileNumberClick?.invoke(importantContactList[absoluteAdapterPosition], absoluteAdapterPosition)
                }
            }
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    fun initLoad(list: List<Contact>) {
        importantContactList.clear()
        importantContactList.addAll(list)
        notifyDataSetChanged()
    }
}