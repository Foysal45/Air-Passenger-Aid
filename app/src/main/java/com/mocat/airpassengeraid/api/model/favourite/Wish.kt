package com.mocat.airpassengeraid.api.model.favourite

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Wish(
    val articleId: Int,
    val articleInfo: ArticleInfo,
    val createAt: String,
    val createBy: Int,
    val deviceId: String,
    val id: Int,
    val isActive: Boolean,
    val isDelete: Boolean,
    val updateAt: String,
    val wish: Int
): Parcelable