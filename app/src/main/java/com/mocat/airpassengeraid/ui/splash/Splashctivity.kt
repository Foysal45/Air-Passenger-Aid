package com.mocat.airpassengeraid.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.messaging.FirebaseMessaging
import com.mocat.airpassengeraid.MainActivity
import com.mocat.airpassengeraid.R
import com.mocat.airpassengeraid.api.model.notification.FcmTokenRequest
import com.mocat.airpassengeraid.databinding.ActivitySplashBinding
import com.mocat.airpassengeraid.ui.home.HomeViewModel
import com.mocat.airpassengeraid.ui.notification.NotificationViewModel
import com.mocat.airpassengeraid.utils.SessionManager
import com.mocat.airpassengeraid.utils.toast
import org.koin.android.ext.android.inject
import timber.log.Timber


@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    private val viewModelNotification: NotificationViewModel by inject()
    private val viewModel: HomeViewModel by inject()
    private var fcm_key: String = ""
    private var device_type: String = "Android"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (supportActionBar != null) {
            supportActionBar?.hide()
        }
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        goToHome()
       // getFcmToken()
        getNotificationCount(0, SessionManager.firebaseToken)

        val animation = AnimationUtils.loadAnimation(this, R.anim.bounce)
        val animation2 = AnimationUtils.loadAnimation(this, R.anim.bounce)

        //SplashScreen Logo Animation
        val backgroundImage: ImageView = findViewById(R.id.civil_aviation_logo)
        val slideAnimation = AnimationUtils.loadAnimation(this, R.anim.bounce2)
        backgroundImage.startAnimation(slideAnimation)


        //SplashScreen Text Animation
        val txtAirPassengerAid: TextView = findViewById(R.id.air_passenger_aid)
        txtAirPassengerAid.startAnimation(animation)


        val txtMinistryOfCivilAviationAndTourism: TextView = findViewById(R.id.ministry_of_civil_aviation_tourism)
        Handler(Looper.getMainLooper()).postDelayed({
            txtMinistryOfCivilAviationAndTourism.visibility = View.VISIBLE
            txtMinistryOfCivilAviationAndTourism.startAnimation(animation2)
        }, 1500)


        val txtAkashPotheJatriSohaika: TextView = findViewById(R.id.akash_pothe_jatri_sohaika)
        Handler(Looper.getMainLooper()).postDelayed({
            txtAkashPotheJatriSohaika.visibility = View.VISIBLE
            txtAirPassengerAid.visibility = View.GONE
            txtMinistryOfCivilAviationAndTourism.visibility = View.GONE
            txtAkashPotheJatriSohaika.startAnimation(animation)
        }, 4000)


        val txtBeshorkariBimanPoribohonOPorjotonMontronaloy: TextView = findViewById(R.id.beshorkari_biman_poribohon_O_porjoton_montronaloy)
        txtBeshorkariBimanPoribohonOPorjotonMontronaloy.startAnimation(animation)
        Handler(Looper.getMainLooper()).postDelayed({
            txtBeshorkariBimanPoribohonOPorjotonMontronaloy.visibility = View.VISIBLE
            txtAirPassengerAid.visibility = View.GONE
            txtMinistryOfCivilAviationAndTourism.visibility = View.GONE
            txtBeshorkariBimanPoribohonOPorjotonMontronaloy.startAnimation(animation2)
        }, 5000)


    }

    //function for get FcmToken and pass it to Notification API
    @SuppressLint("LogNotTimber")
    private fun getFcmToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                fcm_key = task.result
                SessionManager.firebaseToken = fcm_key
                passCredentialForNotification()
                //Toast.makeText(activity, "fcmToken = $fcm_key", Toast.LENGTH_SHORT).show()
                Timber.d("fcmmmmm $fcm_key")
            } else {
                Log.e(MainActivity.TAG, "Failed to get FCM token")
            }
        }
    }

    //function for pass FcmToken and Device Type to API for Notification, Token will pass one time at the App Download time !!
    private fun passCredentialForNotification() {
        // Log.e(MainActivity.TAG, "session" + SessionManager.isPresent)
        if (!SessionManager.isPresent) {
            if (fcm_key != null) {
                val requestBody = FcmTokenRequest(device_type, fcm_key)
                viewModel.passFcmToken(requestBody).observe(this) {
                    //context?.toast("Credentials Pass Successfully.")
                }
            }
            SessionManager.isPresent = true
        }


    }

    //function for get NotificationCount when Notification is Unread from API
    private fun getNotificationCount(is_read: Int, fcm_key: String){
        viewModelNotification.getNotificationList(is_read, fcm_key).observe(this) { list ->
            // Toast.makeText(activity, "fcmToken: ${list.total_unread}", Toast.LENGTH_SHORT).show()
            SessionManager.notificationCount = list.total_unread
        }
    }

    private fun goToHome() {
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this@SplashActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 7000L)

    }

}