package com.mocat.airpassengeraid

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.*
import com.mocat.airpassengeraid.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.messaging.FirebaseMessaging
import com.mocat.airpassengeraid.ui.search.SearchActivity
import com.mocat.airpassengeraid.ui.search.SearchFragment
import com.mocat.airpassengeraid.utils.SessionManager
import com.mocat.airpassengeraid.utils.hideKeyboard
import org.koin.android.ext.android.inject
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var doubleBackToExitPressedOnce = false
    private lateinit var navController: NavController
    private lateinit var navView: NavigationView
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var notificationCountTV: TextView
    private lateinit var searchEditText: TextView
    private var navigationMenuId: Int = 0
    private var menuItem: MenuItem? = null
    private lateinit var notificationImageView: ImageView
    private var fcmToken: String = ""

    //Connectivity
    private var snackBar: Snackbar? = null
    //private lateinit var connectivityReceiver: ConnectivityReceiver

    @SuppressLint("HardwareIds")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navView = findViewById(R.id.nav_view)
        notificationCountTV = findViewById(R.id.tvNotification)
        notificationImageView = findViewById(R.id.notification_id)
        searchEditText = findViewById(R.id.suggestiveSearchET)

        /*setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)*/

        setSupportActionBar(binding.appBarHome.toolbar)
        navController = findNavController(R.id.navHostFragment)
        appBarConfiguration = AppBarConfiguration(setOf(R.id.nav_dashboard), binding.drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navView.setupWithNavController(navController)

        //getNotificationList(0,SessionManager.firebaseToken)
        manageNavigationItemSelection()
        goToNotification()
        goToSearch()

        //val deviceUniqueId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
       // appUpdateManager()
       // checkRemoteConfig()
        //getFcmToken()
        //Toast.makeText(this, "fcmToken: ${SessionManager.notificationCount}", Toast.LENGTH_LONG).show()
        //notificationCountTV.isVisible=false
        if (SessionManager.notificationCount>0){
            notificationCountTV.isVisible=true
            notificationCountTV.text=SessionManager.notificationCount.toString()
        }
        binding.appBarHome.homeId.setOnClickListener{
            onBackPressed()
            //onBackPressed()
        }
    }


    private fun goToSearch(){
        hideKeyboard()
        searchEditText.setOnClickListener {
            val intent = Intent(this@MainActivity, SearchActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
          /*  clearToolbar()
            binding.appBarHome.toolbar.visibility=View.GONE
            navController.navigateUp() // to clear previous navigation history
            navController.navigate(R.id.searchFragment)*/

        }
    }

    private fun getFcmToken(){
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                fcmToken= task.result
                //Toast.makeText(this, "fcmToken: $fcmToken", Toast.LENGTH_LONG).show()
                //Log.d(TAG, "FCM token: $fcmToken")
                // Send the FCM token to the server
            } else {
                Log.e(TAG, "Failed to get FCM token")
            }
        }
    }

    companion object {
        const val TAG = "MainActivity"
    }

    private fun goToNotification(){
        notificationImageView.setOnClickListener {
            clearToolbar()
            binding.appBarHome.homeId.visibility=View.VISIBLE
            binding.appBarHome.tvTitleToolbar.visibility=View.VISIBLE
            binding.appBarHome.tvTitleToolbar.text=getString(R.string.toolbar_title_notification)
            navController.navigate(R.id.notificationFragment)
        }
    }

    //DrawerNavigation Item Click
    private fun manageNavigationItemSelection() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.nav_dashboard -> {
                    initToolbarActions()
                }
            }
        }
        binding.navView.setNavigationItemSelectedListener { item ->
            navigationMenuId = item.itemId
            menuItem = item
            val handled = NavigationUI.onNavDestinationSelected(item, navController)
            if (handled) {
                binding.drawerLayout.closeDrawer(GravityCompat.START)

            } else {
                binding.drawerLayout.closeDrawer(GravityCompat.START)
                when (menuItem!!.itemId) {
                    R.id.nav_home -> {
                        onBackPressed()
                    }

                    R.id.nav_favourite_list -> {
                        clearToolbar()
                        binding.appBarHome.homeId.visibility=View.VISIBLE
                        binding.appBarHome.tvTitleToolbar.visibility=View.VISIBLE
                        binding.appBarHome.tvTitleToolbar.text=getString(R.string.toolbar_title_favourite_list)
                        navController.navigate((R.id.favouriteListFragment))
                    }

                    R.id.nav_airport_map -> {
                        clearToolbar()
                        binding.appBarHome.homeId.visibility=View.VISIBLE
                        binding.appBarHome.tvTitleToolbar.visibility=View.VISIBLE
                        binding.appBarHome.tvTitleToolbar.text=getString(R.string.toolbar_title_airport_map)
                        navController.navigate((R.id.airportMapFragment))
                    }

                    R.id.nav_important_contact -> {
                        clearToolbar()
                        binding.appBarHome.homeId.visibility=View.VISIBLE
                        binding.appBarHome.tvTitleToolbar.visibility=View.VISIBLE
                        binding.appBarHome.tvTitleToolbar.text=getString(R.string.toolbar_title_important_contact)
                        navController.navigate((R.id.importantContactFragment))
                    }

                    R.id.nav_wifi_info -> {
                        clearToolbar()
                        binding.appBarHome.homeId.visibility=View.VISIBLE
                        binding.appBarHome.tvTitleToolbar.visibility=View.VISIBLE
                        binding.appBarHome.tvTitleToolbar.text=getString(R.string.toolbar_title_wifi_information)
                        navController.navigate((R.id.wifiInformationFragment))
                    }

                    R.id.nav_feedback -> {
                        clearToolbar()
                        binding.appBarHome.homeId.visibility=View.VISIBLE
                        binding.appBarHome.tvTitleToolbar.visibility=View.VISIBLE
                        binding.appBarHome.tvTitleToolbar.text=getString(R.string.toolbar_title_feedback)
                        navController.navigate((R.id.feedbackFragment))
                    }

                    R.id.nav_complain -> {
                        goToComplainPage()
                    }


                    R.id.nav_facebook_page -> {
                        goToFaceBookPage()
                    }

                    R.id.nav_rating -> {
                        goToRateApp()
                    }

                    R.id.nav_share -> {
                        shareMyApp(this)
                    }

                    R.id.nav_update_app -> {
                        goToRateApp()
                    }

                    R.id.nav_logout -> {
                        finish()
                        exitProcess(0)
                    }
                }
            }
            return@setNavigationItemSelectedListener true
        }
    }


    //Function for Rating the app in PlayStore
    @SuppressLint("QueryPermissionsNeeded")
    private fun goToRateApp(){
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(""))
        val otherApps: MutableList<ResolveInfo> = this.packageManager!!.queryIntentActivities(intent, 0)
        var agFound = false

        for (app in otherApps) {
            if (app.activityInfo.applicationInfo.packageName.equals("com.foysal.airpassengeraid")) {
                val psComponent = ComponentName(app.activityInfo.applicationInfo.packageName, app.activityInfo.name)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.component = psComponent
                startActivity(intent)
                agFound = true
                break
            }
        }
        if (!agFound) {
            this.openUrlInBrowser("https://play.google.com/store/apps/details?id=com.mocat.airpassengeraid")
        }
    }

    private fun Context.openUrlInBrowser(url: String?) {
        try {
            if (url.isNullOrBlank())
                return

            val intentBuilder = CustomTabsIntent.Builder()
            intentBuilder.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary))
            intentBuilder.setSecondaryToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary))
            val customTabsIntent = intentBuilder.build()
            customTabsIntent.launchUrl(this, Uri.parse(url.trim()))
        } catch (e: Exception) {
            logException(e)
        }
    }

    private fun Any.logException(e: java.lang.Exception) {
        val crashlytics = FirebaseCrashlytics.getInstance()
    }

    //function for share the app to others
    private fun shareMyApp(context: Context) {
        val appName = context.getString(R.string.app_name)
        val appPlayStoreLink = "https://play.google.com/store/apps/details?id=com.mocat.airpassengeraid"

        val sendIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TITLE, appName)
            putExtra(Intent.EXTRA_TEXT, appPlayStoreLink)
        }
        context.startActivity(Intent.createChooser(sendIntent, null))
    }

    //function for set the Title in Toolbar of the Activity or Fragment in the app
    fun setToolbarTitle(title: String) {
        supportActionBar?.title = title
    }

    fun setActionBarTitle(title: String) {
        clearToolbar()
        binding.appBarHome.homeId.visibility = View.VISIBLE
        binding.appBarHome.tvTitleToolbar.visibility = View.VISIBLE
        binding.appBarHome.tvTitleToolbar.text = title
        supportActionBar?.title = title
    }

    private fun clearToolbar() {
        notificationImageView.isVisible = false
        notificationCountTV.isVisible = false
        searchEditText.isVisible = false
    }

    private fun initToolbarActions() {

        notificationImageView.isVisible = true
        notificationCountTV.isVisible = false
        searchEditText.isVisible = true
        binding.appBarHome.homeId.visibility=View.GONE
        binding.appBarHome.tvTitleToolbar.visibility=View.GONE

    }


    //function for double click in back button to exit the app
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {

        when {
            //back button single click to close Navigation Drawer
            binding.drawerLayout.isDrawerOpen(GravityCompat.START) || binding.drawerLayout.isDrawerOpen(GravityCompat.END) -> {
                if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                }
                if (binding.drawerLayout.isDrawerOpen(GravityCompat.END)) {
                    binding.drawerLayout.closeDrawer(GravityCompat.END)
                }
            }

            navController.currentDestination?.id != navController.graph.startDestination -> {
                super.onBackPressed()
            }

            else -> {
                if (doubleBackToExitPressedOnce) {
                    super.onBackPressed()
                    return
                }
                doubleBackToExitPressedOnce = true
                Toast.makeText(this, getString(R.string.press_again_to_exit), Toast.LENGTH_SHORT).show()
                Handler(Looper.getMainLooper()).postDelayed({
                    doubleBackToExitPressedOnce = false
                }, 2000L)
            }
        }
    }


    //Function for go to Civil Aviation FaceBook Page
    @SuppressLint("QueryPermissionsNeeded")
    private fun goToFaceBookPage(){
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(""))
        val otherApps: MutableList<ResolveInfo> = this.packageManager!!.queryIntentActivities(intent, 0)
        var agFound = false

        for (app in otherApps) {
            if (app.activityInfo.applicationInfo.packageName.equals("com.foysal.airpassengeraid")) {
                val psComponent = ComponentName(app.activityInfo.applicationInfo.packageName, app.activityInfo.name)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.component = psComponent
                startActivity(intent)
                agFound = true
                break
            }
        }
        if (!agFound) {
            this.openUrlInBrowser("https://www.facebook.com/mocat.gov.bd?mibextid=LQQJ4d")
        }
    }

    //Function for go to Civil Aviation Complain Page
    @SuppressLint("QueryPermissionsNeeded")
    private fun goToComplainPage(){
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(""))
        val otherApps: MutableList<ResolveInfo> = this.packageManager!!.queryIntentActivities(intent, 0)
        var agFound = false

        for (app in otherApps) {
            if (app.activityInfo.applicationInfo.packageName.equals("com.foysal.airpassengeraid")) {
                val psComponent = ComponentName(app.activityInfo.applicationInfo.packageName, app.activityInfo.name)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.component = psComponent
                startActivity(intent)
                agFound = true
                break
            }
        }
        if (!agFound) {
            this.openUrlInBrowser("https://www.grs.gov.bd/complainWithoutLogin.do")
        }
    }


    override fun onResume() {
        super.onResume()
        hideKeyboard()
        // Get the input method manager
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        // Check if the keyboard is currently shown
        if (imm.isAcceptingText) {
            // Hide the keyboard
            imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        }
    }


    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}