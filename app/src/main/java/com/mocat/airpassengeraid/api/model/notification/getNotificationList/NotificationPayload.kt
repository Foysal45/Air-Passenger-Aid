package com.mocat.airpassengeraid.api.model.notification.getNotificationList

data class NotificationPayload(
    val notification: List<Notification>,
    val total_unread: Int
)