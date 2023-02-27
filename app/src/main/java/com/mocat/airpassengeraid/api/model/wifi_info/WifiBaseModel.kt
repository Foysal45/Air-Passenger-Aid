package com.mocat.airpassengeraid.api.model.wifi_info

data class WifiBaseModel(
    val error: Error,
    val message: String,
    val metadata: Metadata,
    val nonce: Long,
    val payload: Payload,
    val status: Int
)