package com.mocat.airpassengeraid.api.model.notification.getNotificationList

data class Notification(
    val body: String,
    val createAt: String,
    val createBy: Int,
    val deleteAt: Any,
    val deleteBy: Any,
    val device_id: String,
    val id: Int,
    val isActive: Boolean,
    val isDelete: Boolean,
    val read_status: Int,
    val title: String,
    val updateAt: String,
    val updateBy: Any
)