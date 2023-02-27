package com.mocat.airpassengeraid.ui.home.home_sub_menu

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mocat.airpassengeraid.MainActivity
import com.mocat.airpassengeraid.R
import com.mocat.airpassengeraid.api.model.home.Menu
import com.mocat.airpassengeraid.databinding.FragmentHomeSubMenuBinding
import com.mocat.airpassengeraid.ui.home.HomeViewModel
import com.mocat.airpassengeraid.utils.ViewState
import com.mocat.airpassengeraid.utils.hideKeyboard
import com.mocat.airpassengeraid.utils.toast
import org.koin.android.ext.android.inject
import timber.log.Timber

class HomeSubMenuFragment : Fragment() {
    private var binding: FragmentHomeSubMenuBinding? = null
    private var dataAdapter: HomeSubMenuListAdapter = HomeSubMenuListAdapter()
    private val viewModel: HomeViewModel by inject()
    private var page: Int = 1
    private var limit: Int = 20
    private var lang: String = "bn"
    private var model: Menu? = null
    private var dynamicTitle: String = ""


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        return FragmentHomeSubMenuBinding.inflate(inflater, container, false).also {
            binding = it
        }.root

    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView()
        getSubMenuData()
        getHomeSubContentList(page, limit, lang, model?.id)
        initClickLister()
    }


    //Set Toolbar Title Dynamically as per navigated Recycler Item Name
    override fun onResume() {
        if (model?.categoryDetails?.isNotEmpty() == true){
            dynamicTitle = model?.categoryDetails?.first()?.name.toString()
        }
        (activity as MainActivity?)?.setActionBarTitle(dynamicTitle)
        super.onResume()
    }


    private fun initView() {
        binding?.homeSubFragmentRecyclerView?.let { view ->
            with(view) {
                setHasFixedSize(true)
                itemAnimator = androidx.recyclerview.widget.DefaultItemAnimator()
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
                        binding?.progressBarHomeSubFragment?.visibility = View.VISIBLE
                    } else {
                        binding?.progressBarHomeSubFragment?.visibility = View.GONE
                    }
                }

                else -> {}
            }
        }

    }

    //function ClickListener
    private fun initClickLister() {
        dataAdapter.onItemClick = { model, _ ->
            if (model.child.isEmpty()){
                goToMenuDetailsFragment(model)
            }
        }

        //RecyclerView Sub Item Click to go to Details Fragment
        dataAdapter.onSubSubItemClick = {model1, _->
            if (model1.child.isNotEmpty()){
                findNavController().navigate(R.id.action_HomeSubMenuFragment_to_menuDetailsFragment)
            }else{
                Toast.makeText(activity, resources.getString(R.string.no_data_found), Toast.LENGTH_SHORT).show()
            }
        }

    }

    //function for fetch the HomeSubMenu/ContentList
    private fun getHomeSubContentList(page: Int, limit: Int, lang: String, parentId: Int?) {

        viewModel.getHomeSubContentList(page, limit, lang, parentId).observe(viewLifecycleOwner) { list ->
            val dataList=list.size
            Timber.d("subMenuListSize: $dataList")
            if (list.isEmpty()){
                binding?.emptyView?.visibility = View.VISIBLE
            }else{
                binding?.emptyView?.visibility = View.GONE
                dataAdapter.initLoad(list)
            }
        }
    }

    //transfer model to the HomeSubFragment by bundle
    private fun goToMenuDetailsFragment(model: Menu){
        val bundle = bundleOf("model" to model)
        Timber.d("modelMenu $bundle")
        findNavController().navigate(R.id.action_HomeSubMenuFragment_to_menuDetailsFragment,bundle)
    }

    //here received bundle model from HomeFragment
    private fun getSubMenuData(){
        val bundle: Bundle? = arguments
        bundle?.let {
            this.model = it.getParcelable("model")
            Timber.d("bundleModel $model")
        }

    }



    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}