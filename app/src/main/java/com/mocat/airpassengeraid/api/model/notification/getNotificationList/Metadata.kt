package com.mocat.airpassengeraid.api.model.notification.getNotificationList

data class Metadata(
    val limit: Int,
    val page: Int,
    val totalCount: Int,
    val totalPage: Int
)