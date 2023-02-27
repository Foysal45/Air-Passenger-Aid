package com.mocat.airpassengeraid.api.model.airport_map

data class Map(
    val createAt: String,
    val createBy: Int,
    val deleteAt: Any,
    val deleteBy: Any,
    val id: Int,
    val isActive: Boolean,
    val isDelete: Boolean,
    val mapDetailsInfo: List<MapDetailsInfo>,
    val mapMediaLinkInfo: List<MapMediaLinkInfo>,
    val serial: Int,
    val updateAt: String,
    val updateBy: Any
)