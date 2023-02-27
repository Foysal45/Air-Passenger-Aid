package com.mocat.airpassengeraid.api.model.notification

data class FcmToken(
    val error: Error,
    val message: String,
    val metadata: Metadata,
    val nonce: Long,
    val payload: Payload,
    val status: Int
)