package com.mocat.airpassengeraid.ui.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.mocat.airpassengeraid.databinding.FragmentNotificationBinding
import com.mocat.airpassengeraid.utils.SessionManager
import com.mocat.airpassengeraid.utils.ViewState
import com.mocat.airpassengeraid.utils.hideKeyboard
import com.mocat.airpassengeraid.utils.toast
import org.koin.android.ext.android.inject

class NotificationFragment : Fragment() {

    private var binding: FragmentNotificationBinding? = null
    private var dataAdapter: NotificationListAdapter = NotificationListAdapter()
    private val viewModel: NotificationViewModel by inject()
    private var isRead: Int =
        1 //is_read all time 1 means notification fragment a gelei notification list read dhora hobe and api te read 1 jabe
    private val viewModelNotification: NotificationViewModel by inject()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //(activity as MainActivity).supportActionBar?.title = getString(R.string.toolbar_title_notification)
        //(requireActivity() as AppCompatActivity).supportActionBar?.title = "Home"
        // (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        // (requireActivity() as AppCompatActivity).supportActionBar?.title = getString(R.string.toolbar_title_notification)
        return FragmentNotificationBinding.inflate(inflater, container, false).also {
            binding = it
        }.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()

        if (SessionManager.firebaseToken.isNotEmpty()) {
            getNotificationList(isRead, SessionManager.firebaseToken)
        }
        binding?.swipeRefreshNotification?.setOnRefreshListener {
            getNotificationList(isRead, SessionManager.firebaseToken)
        }
        getNotificationCount(0, SessionManager.firebaseToken)


    }

    private fun initView() {
        binding?.recyclerViewNotification?.let { view ->
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
                        binding?.progressNotification?.visibility = View.VISIBLE
                    } else {
                        binding?.progressNotification?.visibility = View.GONE
                    }
                }
                else -> {}
            }
        }
    }

    //function for get NotificationCount when Notification is Unread from API
    private fun getNotificationCount(is_read: Int, fcm_key: String) {
        viewModelNotification.getNotificationList(is_read, fcm_key).observe(viewLifecycleOwner) { list ->
               // context?.toast("Notification Count Updated")
                SessionManager.notificationCount = list.total_unread
                // Toast.makeText(activity, "fcmToken: ${list.total_unread}", Toast.LENGTH_SHORT).show()
                //SessionManager.notificationCount = list.total_unread
            }
    }


    //function for get NotificationList from API
    private fun getNotificationList(is_read: Int, fcm_key: String) {
        binding?.swipeRefreshNotification?.isRefreshing = false
        viewModel.getNotificationList(is_read, fcm_key).observe(viewLifecycleOwner) { list ->
            if (list.notification.isEmpty()) {
                binding?.emptyViewNotification?.visibility = View.VISIBLE
            } else {
                binding?.emptyViewNotification?.visibility = View.GONE
                binding?.swipeRefreshNotification?.isRefreshing = false
                dataAdapter.initLoad(list)
            }
        }
    }

    /* override fun onResume() {
         super.onResume()
         //(activity as MainActivity?)?.setToolbarTitle("test")
         //(activity as MainActivity?)?.setActionBarTitle("Your title")
         //(requireActivity() as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(true)
         //(activity as MainActivity).setToolbarTitle(getString(R.string.toolbar_title_notification))
     }*/

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}