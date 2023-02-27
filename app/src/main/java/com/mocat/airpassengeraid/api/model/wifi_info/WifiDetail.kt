package com.mocat.airpassengeraid.api.model.wifi_info

data class WifiDetail(
    val createAt: String,
    val createBy: Int,
    val deleteAt: Any,
    val deleteBy: Any,
    val id: Int,
    val isActive: Boolean,
    val isDelete: Boolean,
    val languageId: Int,
    val name: String,
    val password: String,
    val shortDesc: String,
    val ssid: String,
    val updateAt: String,
    val updateBy: Int,
    val wifiId: Int
)