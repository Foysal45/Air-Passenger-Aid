package com.mocat.airpassengeraid.api.model.home

import android.os.Parcelable
import kotlinx.coroutines.internal.PrepareOp
import kotlinx.parcelize.Parcelize

@Parcelize
data class Media(
    val categoryId: Int,
    val createAt: String,
    val createBy: Int,
    val id: Int,
    val isActive: Boolean,
    val isDelete: Boolean,
    val mediaId: Int,
    val mediaInfo: MediaInfo,
    val updateAt: String,
): Parcelable