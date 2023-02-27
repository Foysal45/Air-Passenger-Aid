package com.mocat.airpassengeraid.api.model.wishlist

data class WishSubmitResponse(
    val error: Error,
    val message: String,
    val nonce: Long,
    val status: Int
)