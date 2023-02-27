package com.mocat.airpassengeraid.api.model.home

data class BaseModel(
    val error: Error,
    val message: String,
    val nonce: Long,
    val payload: Payload,
    val status: Int
)