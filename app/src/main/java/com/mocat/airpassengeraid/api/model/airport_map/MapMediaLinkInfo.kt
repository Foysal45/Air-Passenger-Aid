package com.mocat.airpassengeraid.api.model.airport_map

data class MapMediaLinkInfo(
    val createAt: String,
    val createBy: Int,
    val deleteAt: Any,
    val deleteBy: Any,
    val id: Int,
    val isActive: Boolean,
    val isDelete: Boolean,
    val mapId: Int,
    val mediaId: Int,
    val mediaInfo: MediaInfo,
    val updateAt: String,
    val updateBy: Any
)