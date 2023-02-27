package com.mocat.airpassengeraid.api.model.feedback

data class FeedbackTypeDetail(
    val createAt: String,
    val createBy: Int,
    val deleteAt: Any,
    val deleteBy: Any,
    val feedbackTypeId: Int,
    val id: Int,
    val isActive: Boolean,
    val isDelete: Boolean,
    val languageId: Int,
    val name: String,
    val shortDesc: String,
    val updateAt: String,
    val updateBy: Any
)