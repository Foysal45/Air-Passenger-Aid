package com.mocat.airpassengeraid.api.model.airport_map

data class MapDetailsInfo(
    val createAt: String,
    val createBy: Int,
    val deleteAt: Any,
    val deleteBy: Any,
    val id: Int,
    val isActive: Boolean,
    val isDelete: Boolean,
    val languageId: Int,
    val mapId: Int,
    val name: String,
    val updateAt: String,
    val updateBy: Int
)