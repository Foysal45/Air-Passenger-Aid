package com.mocat.airpassengeraid.api.model.home.details

data class Detail(
    val categoryId: Int,
    val createAt: String,
    val createBy: Int,
    val deleteAt: Any,
    val deleteBy: Any,
    val id: Int,
    val isActive: Boolean,
    val isDelete: Boolean,
    val languageId: Int,
    val name: String,
    val shortDesc: String,
    val updateAt: String,
    val updateBy: Any
)