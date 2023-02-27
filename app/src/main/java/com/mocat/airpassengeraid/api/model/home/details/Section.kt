package com.mocat.airpassengeraid.api.model.home.details

data class Section(
    val address: Any,
    val articleType: String,
    val categoryId: Int,
    val categoryIds: Any,
    val categoryInfo: CategoryInfo,
    val createAt: String,
    val createBy: Int,
    val deleteAt: Any,
    val deleteBy: Any,
    val email: Any,
    val id: Int,
    val isActive: Boolean,
    val isDelete: Boolean,
    val latitude: Any,
    val longitude: Any,
    val media: List<Media>,
    val phone: Any,
    val popularPoint: String,
    val schedule_on: Any,
    val section: SectionX,
    val slug: String,
    val updateAt: String,
    val updateBy: Int
)