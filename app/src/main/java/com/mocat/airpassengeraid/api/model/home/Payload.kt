package com.mocat.airpassengeraid.api.model.home

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Payload(
    val menus: List<Menu>
): Parcelable