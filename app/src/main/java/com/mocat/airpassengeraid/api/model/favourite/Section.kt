package com.mocat.airpassengeraid.api.model.favourite

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Section(
    val articleId: Int,
    val createAt: String,
    val createBy: Int,
    val details: List<Detail>,
    val id: Int,
    val isActive: Boolean,
    val isDelete: Boolean,
    val updateAt: String,
): Parcelable