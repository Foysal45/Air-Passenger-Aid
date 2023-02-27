package com.mocat.airpassengeraid.api.model.home.details

data class DetailsBase(
    val error: Error,
    val message: String,
    val nonce: Long,
    val payload: Payload,
    val status: Int
)