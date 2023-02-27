package com.mocat.airpassengeraid.api.model.feedback.request_body

data class FeedbackSubmitRequest(
    val email: String,
    val feedback: String,
    val feedback_type_id: Int,
    val name: String,
    val phone: String
)