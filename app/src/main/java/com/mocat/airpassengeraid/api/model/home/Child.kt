package com.mocat.airpassengeraid.api.model.home

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Child(
    val childCategoryDetails: List<ChildCategoryDetails>,
    val createAt: String,
    val createBy: Int,
    val id: Int,
    val isActive: Boolean,
    val isDelete: Boolean,
    val media: List<Media>,
    val parentId: Int,
    val serial: Int,
    val slug: String,
    val updateAt: String,
    val updateBy: Int
): Parcelable