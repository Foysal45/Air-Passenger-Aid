package com.mocat.airpassengeraid.api.model.feedback

data class FeedbackType(
    val createAt: String,
    val createBy: Int,
    val deleteAt: Any,
    val deleteBy: Any,
    val feedbackTypeDetails: List<FeedbackTypeDetail>,
    val id: Int,
    val isActive: Boolean,
    val isDelete: Boolean,
    val parentId: Any,
    val serial: Int,
    val updateAt: String,
    val updateBy: Any
)