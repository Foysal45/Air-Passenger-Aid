package com.mocat.airpassengeraid.api.model.home

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ChildCategoryDetails(
    val categoryId: Int,
    val createAt: String,
    val createBy: Int,
    val id: Int,
    val isActive: Boolean,
    val isDelete: Boolean,
    val languageId: Int,
    val name: String,
    val shortDesc: String,
    val updateAt: String,
    val updateBy: Int
): Parcelable