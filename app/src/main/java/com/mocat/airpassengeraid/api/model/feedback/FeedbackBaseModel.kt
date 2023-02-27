package com.mocat.airpassengeraid.api.model.feedback

data class FeedbackBaseModel(
    val error: Error,
    val message: String,
    val nonce: Long,
    val payload: Payload,
    val status: Int
)