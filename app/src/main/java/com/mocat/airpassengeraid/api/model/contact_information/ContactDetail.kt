package com.mocat.airpassengeraid.api.model.contact_information

data class ContactDetail(
    val contactId: Int,
    val contactInfo: ContactInfo,
    val createAt: String,
    val createBy: Int,
    val deleteAt: Any,
    val deleteBy: Any,
    val email: String,
    val id: Int,
    val isActive: Boolean,
    val isDelete: Boolean,
    val languageId: Int,
    val name: String,
    val phonenumber: String,
    val telephone: String,
    val updateAt: String,
    val updateBy: Any
)