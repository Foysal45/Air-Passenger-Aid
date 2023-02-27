package com.mocat.airpassengeraid.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.annotation.NonNull
import androidx.core.content.edit
import com.mocat.airpassengeraid.BuildConfig

object SessionManager {

    private const val prefName = "${BuildConfig.APPLICATION_ID}.session"
    private lateinit var pref: SharedPreferences


    fun init(@NonNull context: Context) {
        pref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
    }

    var firebaseToken: String
        get() {
            return pref.getString("firebaseToken", "")!!
        }
        set(value) {
            pref.edit {
                putString("firebaseToken", value)
            }
        }


    var isPresent: Boolean
        get() {
            return pref.getBoolean("isPassedToken", false)
        }
        set(value) {
            pref.edit {
                putBoolean("isPassedToken", value)
            }
        }


    var notificationCount: Int
        get() {
            return pref.getInt("notificationCount", 0)
        }
        set(value) {
            pref.edit {
                putInt("notificationCount", value)
            }
        }

}