package com.mocat.airpassengeraid.api.model.favourite

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MediaInfo(
    val createAt: String,
    val createBy: Int,
    val group: String,
    val id: Int,
    val isActive: Boolean,
    val isDelete: Boolean,
    val order: Int,
    val source: String,
    val type: String,
    val updateAt: String,
    val updateBy: Int
): Parcelable