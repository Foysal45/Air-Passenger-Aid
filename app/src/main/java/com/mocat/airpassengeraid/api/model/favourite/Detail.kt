package com.mocat.airpassengeraid.api.model.favourite

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Detail(
    val createAt: String,
    val createBy: Int,
    val description: String,
    val id: Int,
    val isActive: Boolean,
    val isDelete: Boolean,
    val languageId: Int,
    val subTitle: String,
    val title: String,
    val updateAt: String
): Parcelable