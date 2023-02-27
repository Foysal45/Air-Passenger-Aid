package com.mocat.airpassengeraid.api.model.favourite

data class WishListBaseModel(
    val error: Error,
    val message: String,
    val metadata: Metadata,
    val nonce: Long,
    val payload: Payload,
    val status: Int
)