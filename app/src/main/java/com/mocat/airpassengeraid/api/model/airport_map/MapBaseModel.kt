package com.mocat.airpassengeraid.api.model.airport_map

data class MapBaseModel(
    val error: Error,
    val message: String,
    val nonce: Long,
    val payload: Payload,
    val status: Int
)