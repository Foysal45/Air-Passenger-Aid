package com.mocat.airpassengeraid.ui.favourite

import android.annotation.SuppressLint
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mocat.airpassengeraid.MainActivity
import com.mocat.airpassengeraid.R
import com.mocat.airpassengeraid.api.model.favourite.Wish
import com.mocat.airpassengeraid.api.model.home.Menu
import com.mocat.airpassengeraid.databinding.FragmentFavouriteListBinding
import com.mocat.airpassengeraid.ui.feedback.FeedbackViewModel
import com.mocat.airpassengeraid.utils.ViewState
import com.mocat.airpassengeraid.utils.hideKeyboard
import com.mocat.airpassengeraid.utils.toast
import org.koin.android.ext.android.inject
import timber.log.Timber

class FavouriteListFragment : Fragment() {


    private var binding: FragmentFavouriteListBinding? = null
    private var dataAdapter: FavouriteContentListAdapter = FavouriteContentListAdapter()
    private val viewModel: FavouriteContentListViewModel by inject()

    private var deviceId : String = ""
    private var lang: String = "bn"
    private var wishFlag: String = "yes"


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return FragmentFavouriteListBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    @SuppressLint("HardwareIds")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (activity as MainActivity?)?.setActionBarTitle(getString(R.string.toolbar_title_favourite_list))
        deviceId = Settings.Secure.getString(requireActivity().contentResolver, Settings.Secure.ANDROID_ID)
        initView()
        getFavouriteArticleList(deviceId, lang)
        initClickLister()

    }

    private fun initView() {
        binding?.favouriteListRecyclerView?.let { view ->
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
                        binding?.progressBarFavouriteList?.visibility = View.VISIBLE
                    } else {
                        binding?.progressBarFavouriteList?.visibility = View.GONE
                    }
                }
                else -> {}
            }
        }
    }

    //function ClickListener
    private fun initClickLister() {
        dataAdapter.onItemClick = { model, _ ->
            goToMenuDetailsFragment(model)
        }

    }

    //here call Favourite List API and fetch data
    private fun getFavouriteArticleList(deviceId: String, lang: String){

        viewModel.getFavouriteArticleList(deviceId, lang).observe(viewLifecycleOwner) { list ->
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


    //transfer model to the MenuDetailsFragment by bundle and pass a wishFlag for check in DetailsPage when there show Details from WishList or General Menu List
    private fun goToMenuDetailsFragment(model: Wish){
        val bundle = bundleOf("wishList" to model,)
        Timber.d("modelWish $bundle")
        bundle.putString("wishFlag", wishFlag)
        // Toast.makeText(activity, model.categoryDetails.first().name, Toast.LENGTH_SHORT).show()
        findNavController().navigate(R.id.action_favouriteListFragment_to_menuDetailsFragment,bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}