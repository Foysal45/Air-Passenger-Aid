package com.mocat.airpassengeraid.api.model.feedback.request_body

data class FeedbackSubmitResponse(
    val error: Error,
    val message: String,
    val nonce: Long,
    val status: Int
)