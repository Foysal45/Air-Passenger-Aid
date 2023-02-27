package com.mocat.airpassengeraid.api.model.home

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Menu(
    val categoryDetails: List<CategoryDetail>,
    val child: List<Child>,
    val createAt: String,
    val createBy: Int,
    val id: Int,
    val isActive: Boolean,
    val isDelete: Boolean,
    val media: List<MediaX>,
    val parentId: Int,
    val serial: Int,
    val slug: String,
    val updateAt: String,
    val updateBy: Int
): Parcelable