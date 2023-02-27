package com.mocat.airpassengeraid.api.model.home.details

data class Media(
    val createAt: String,
    val createBy: Int,
    val deleteAt: Any,
    val deleteBy: Any,
    val id: Int,
    val isActive: Boolean,
    val isDelete: Boolean,
    val mediaId: Int,
    val mediaInfo: MediaInfo,
    val updateAt: String,
    val updateBy: Any
)