package com.mocat.airpassengeraid.di

import android.annotation.SuppressLint
import android.app.Application
import com.google.firebase.messaging.FirebaseMessaging
import com.mocat.airpassengeraid.utils.SessionManager
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class MainApplication: Application() {

    @SuppressLint("HardwareIds")
    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())
        SessionManager.init(applicationContext)
        startKoin {
            androidContext(this@MainApplication)
            modules(listOf(appModule))
        }

        /*FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Timber.d(task.exception)
            } else {
                val token = task.result
                SessionManager.firebaseToken = token
                Timber.d("FirebaseToken:\n$token")
            }
        }*/

    }
}