package com.mocat.airpassengeraid.api.model.home.details

data class CategoryInfo(
    val createAt: String,
    val createBy: Int,
    val deleteAt: Any,
    val deleteBy: Any,
    val detail: Detail,
    val id: Int,
    val isActive: Boolean,
    val isDelete: Boolean,
    val parentId: Int,
    val serial: Int,
    val slug: String,
    val updateAt: String,
    val updateBy: Int
)