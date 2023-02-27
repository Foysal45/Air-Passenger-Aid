package com.mocat.airpassengeraid.api.model.home.details

data class DetailX(
    val createAt: String,
    val createBy: Int,
    val deleteAt: Any,
    val deleteBy: Any,
    val description: String,
    val id: Int,
    val isActive: Boolean,
    val isDelete: Boolean,
    val languageId: Int,
    val subTitle: String,
    val title: String,
    val updateAt: String,
    val updateBy: Any
)