package com.mocat.airpassengeraid.ui.wifi_info

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.mocat.airpassengeraid.databinding.FragmentWifiInformationBinding
import com.mocat.airpassengeraid.utils.ViewState
import com.mocat.airpassengeraid.utils.hideKeyboard
import com.mocat.airpassengeraid.utils.toast
import org.koin.android.ext.android.inject
import timber.log.Timber

class WifiInformationFragment : Fragment() {

    private var binding: FragmentWifiInformationBinding? = null
    private var dataAdapter: WifiInformationListAdapter = WifiInformationListAdapter()
    private val viewModel: WifiInfoViewModel by inject()
    private var lang: String = "bn"


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return FragmentWifiInformationBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        initView()
        getWifiInfoList(lang)
    }

    private fun initView() {
        binding?.wifiInformationRecyclerView?.let { view ->
            with(view) {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(requireContext())
                adapter = dataAdapter
            }
        }
        viewModel.viewState.observe(viewLifecycleOwner){ state ->
            when (state) {
                is ViewState.ShowMessage -> {
                    requireContext().toast(state.message)
                }
                is ViewState.KeyboardState -> {
                    hideKeyboard()
                }
                is ViewState.ProgressState -> {
                    if (state.isShow) {
                        binding?.progressBarWifiInformation?.visibility = View.VISIBLE
                    } else {
                        binding?.progressBarWifiInformation?.visibility = View.GONE
                    }
                }
                else -> {}
            }
        }
    }

    private fun getWifiInfoList(lang: String){

        viewModel.getWifiInfoList(lang).observe(viewLifecycleOwner) { list ->
            val dataList=list.size
            Timber.d("size: $dataList")
            //Toast.makeText(activity, "Total List = $dataList", Toast.LENGTH_SHORT).show()

            //sorted list by last updated date
            //val sort = list.sortedWith(compareByDescending { it.id})
            //Timber.d("response:${dataAdapter.initLoad(list)}")
            if (list.isEmpty()){
                binding?.emptyView?.visibility = View.VISIBLE
            }else{
                binding?.emptyView?.visibility = View.GONE
                dataAdapter.initLoad(list)
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}