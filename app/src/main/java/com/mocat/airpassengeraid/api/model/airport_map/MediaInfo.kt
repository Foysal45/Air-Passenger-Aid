package com.mocat.airpassengeraid.api.model.airport_map

data class MediaInfo(
    val createAt: String,
    val createBy: Int,
    val deleteAt: Any,
    val deleteBy: Any,
    val group: String,
    val id: Int,
    val isActive: Boolean,
    val isDelete: Boolean,
    val order: Int,
    val source: String,
    val type: String,
    val updateAt: String,
    val updateBy: Any
)