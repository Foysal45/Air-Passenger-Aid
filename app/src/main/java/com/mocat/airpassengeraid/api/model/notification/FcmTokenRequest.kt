package com.mocat.airpassengeraid.api.model.notification

data class FcmTokenRequest(
    val device_type: String,
    val fcm_key: String
)