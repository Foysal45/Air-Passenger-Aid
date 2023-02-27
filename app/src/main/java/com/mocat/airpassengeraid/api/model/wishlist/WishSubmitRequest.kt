package com.mocat.airpassengeraid.api.model.wishlist

data class WishSubmitRequest(
    val wish: Int,
    val articleId: Int,
    val deviceId: String
)