package com.mocat.airpassengeraid.api.model.notification.getNotificationList

data class NotificationBaseModel(
    val error: Error,
    val message: String,
    val metadata: Metadata,
    val nonce: Long,
    val payload: NotificationPayload,
    val status: Int
)