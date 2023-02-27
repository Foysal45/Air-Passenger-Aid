package com.mocat.airpassengeraid.api.model.contact_information

data class ContactInfo(
    val createAt: String,
    val createBy: Int,
    val deleteAt: Any,
    val deleteBy: Any,
    val id: Int,
    val isActive: Boolean,
    val isDelete: Boolean,
    val parentId: Any,
    val serial: Int,
    val updateAt: String,
    val updateBy: Any
)