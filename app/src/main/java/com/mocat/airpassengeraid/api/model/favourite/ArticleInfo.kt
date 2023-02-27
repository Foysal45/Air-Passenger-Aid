package com.mocat.airpassengeraid.api.model.favourite

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ArticleInfo(
    val articleType: String,
    val categoryId: Int,
    val createAt: String,
    val createBy: Int,
    val id: Int,
    val isActive: Boolean,
    val isDelete: Boolean,
    val media: List<Media>,
    val popularPoint: String,
    val sections: List<Section>,
    val slug: String,
    val updateAt: String,
    val updateBy: Int
): Parcelable