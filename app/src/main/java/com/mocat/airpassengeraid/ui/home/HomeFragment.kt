package com.mocat.airpassengeraid.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.messaging.FirebaseMessaging
import com.mocat.airpassengeraid.MainActivity
import com.mocat.airpassengeraid.R
import com.mocat.airpassengeraid.api.model.home.Menu
import com.mocat.airpassengeraid.api.model.notification.FcmTokenRequest
import com.mocat.airpassengeraid.databinding.FragmentHomeBinding
import com.mocat.airpassengeraid.ui.notification.NotificationViewModel
import com.mocat.airpassengeraid.utils.SessionManager
import com.mocat.airpassengeraid.utils.ViewState
import com.mocat.airpassengeraid.utils.hideKeyboard
import com.mocat.airpassengeraid.utils.toast
import org.koin.android.ext.android.inject
import timber.log.Timber

class HomeFragment : Fragment() {

    private var binding: FragmentHomeBinding? = null
    private var dataAdapter: HomeContentListAdapter = HomeContentListAdapter()
    private val viewModel: HomeViewModel by inject()
    private val viewModelNotification: NotificationViewModel by inject()
    private lateinit var sessionManager: SessionManager

    private var page: Int = 1
    private var limit: Int = 20
    private var lang: String = "bn"
    private var fcm_key: String = ""
    private var device_type: String = "Android"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return FragmentHomeBinding.inflate(inflater, container, false).also {
            binding = it
        }.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        sessionManager = SessionManager //initialize SessionManager
        initView()
        getHomeContentList(page, limit, lang, null)
        initClickLister()
        getFcmToken()
    }


    private fun initView() {
        binding?.contentListRecyclerView?.let { view ->
            with(view) {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(requireContext())
                adapter = dataAdapter
            }
        }
        viewModel.viewState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is ViewState.ShowMessage -> {
                    requireContext().toast(state.message)
                }
                is ViewState.KeyboardState -> {
                    hideKeyboard()
                }
                is ViewState.ProgressState -> {
                    if (state.isShow) {
                        binding?.progressBar?.visibility = View.VISIBLE
                    } else {
                        binding?.progressBar?.visibility = View.GONE
                    }
                }

                else -> {}
            }
        }
    }

    //function ClickListener
    private fun initClickLister() {
        dataAdapter.onItemClick = { model, _ ->
            goToHomeSubMenuFragment(model)
        }

    }

    //function for get NotificationCount when Notification is Unread from API
    private fun getNotificationCount(is_read: Int, fcm_key: String){
        viewModelNotification.getNotificationList(is_read, fcm_key).observe(viewLifecycleOwner) { list ->
            // Toast.makeText(activity, "fcmToken: ${list.total_unread}", Toast.LENGTH_SHORT).show()
            SessionManager.notificationCount = list.total_unread
        }
    }

    //function for fetch the HomeContentList
    private fun getHomeContentList(page: Int, limit: Int, lang: String, parentId: Int?) {

        viewModel.getHomeContentList(page, limit, lang, parentId).observe(viewLifecycleOwner) { list ->
                val dataList = list.size
                Timber.d("size: $dataList")
                //Toast.makeText(activity, "Total List = $dataList", Toast.LENGTH_SHORT).show()
                //sorted list by last updated date
                //val sort = list.sortedWith(compareByDescending { it.createAt})
                //Timber.d("response:${dataAdapter.initLoad(list)}")
                if (list.isEmpty()) {
                    binding?.emptyView?.visibility = View.VISIBLE
                } else {
                    binding?.emptyView?.visibility = View.GONE
                    dataAdapter.initLoad(list)
                }
            }
    }

    //transfer model to the HomeSubFragment by bundle
    private fun goToHomeSubMenuFragment(model: Menu) {
        val bundle = bundleOf("model" to model)
        Timber.d("modelMenu $bundle")
        // Toast.makeText(activity, model.categoryDetails.first().name, Toast.LENGTH_SHORT).show()
        findNavController().navigate(R.id.action_nav_dashboard_to_HomeSubMenuFragment, bundle)
    }

    //function for get FcmToken and pass it to Notification API
    @SuppressLint("LogNotTimber")
    private fun getFcmToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                fcm_key = task.result
                //session a firebase token save koreci karon notification fragment a notification dekhanor jonno token pass kora lagbe
                SessionManager.firebaseToken = fcm_key
                passCredentialForNotification()
               // Toast.makeText(activity, "fcmToken = $fcm_key", Toast.LENGTH_SHORT).show()
                Timber.d("fcmmmmm $fcm_key")
            } else {
                Log.e(MainActivity.TAG, "Failed to get FCM token")
            }
        }
    }

    //function for pass FcmToken and Device Type to API for Notification, Token will pass one time at the App Download time !!
    private fun passCredentialForNotification() {
       // Log.e(MainActivity.TAG, "session" + SessionManager.isPresent)
        //ei api ta 1 bar e call hobe tai session a isPresent check koresi
        if (!SessionManager.isPresent) {
            if (fcm_key != null) {
                val requestBody = FcmTokenRequest(device_type, fcm_key)
                viewModel.passFcmToken(requestBody).observe(viewLifecycleOwner) {
                    context?.toast("Credentials Pass Successfully.")
                }
            }
            SessionManager.isPresent = true
        }


    }
    override fun onResume() {
        super.onResume()
        hideKeyboard()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}