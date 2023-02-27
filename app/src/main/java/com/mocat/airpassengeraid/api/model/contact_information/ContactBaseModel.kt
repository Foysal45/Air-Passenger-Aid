package com.mocat.airpassengeraid.api.model.contact_information

data class ContactBaseModel(
    val error: Error,
    val message: String,
    val nonce: Long,
    val payload: Payload,
    val status: Int
)