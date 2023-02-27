package com.mocat.airpassengeraid.api.model.home.details

data class SectionX(
    val articleId: Int,
    val createAt: String,
    val createBy: Int,
    val deleteAt: Any,
    val deleteBy: Any,
    val details: List<DetailX>,
    val id: Int,
    val isActive: Boolean,
    val isDelete: Boolean,
    val updateAt: String,
    val updateBy: Any
)