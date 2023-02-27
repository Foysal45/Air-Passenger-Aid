package com.mocat.airpassengeraid.ui.search

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mocat.airpassengeraid.R
import com.mocat.airpassengeraid.databinding.ActivitySearchBinding
import com.mocat.airpassengeraid.ui.home.HomeViewModel
import com.mocat.airpassengeraid.utils.*
import org.koin.android.ext.android.inject
import timber.log.Timber


class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private var dataAdapter: SearchListAdapter = SearchListAdapter()
    private var workRunnable: Runnable? = null
    private var handler = Handler(Looper.getMainLooper())
    private val viewModel: HomeViewModel by inject()
    private var page: Int = 1
    private var limit: Int = 20
    private var lang: String = "bn"

    @SuppressLint("HardwareIds")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        initClickLister()
        manageSearch()

        binding.suggestiveSearchET.requestFocus()

    }

    private fun initView() {
        binding.menuListRv.let { view ->
            with(view) {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(this@SearchActivity)
                adapter = dataAdapter
            }
        }

    }

    //function ClickListener
    private fun initClickLister() {
        binding.backToHome.setOnClickListener {
            onBackPressed()
            hideKeyboard()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }

        dataAdapter.onItemClick = {section, _->
            /*if (section.categoryInfo.detail !=null){
                findNavController().navigate(R.id.action_HomeSubMenuFragment_to_menuDetailsFragment)
            }else{
                Toast.makeText(this, resources.getString(R.string.no_data_found), Toast.LENGTH_SHORT).show()
            }*/
        }
    }

    //function for fetch the ContentList
    private fun menuSearch(page: Int, limit: Int, lang: String, title: String) {

        viewModel.menuSearch(page, limit, lang, title).observe(this) { list ->
            val dataList = list.size
            Timber.d("size: $dataList")
            dataAdapter.initLoad(list)
        }
    }

    private fun manageSearch() {

        binding.suggestiveSearchET.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            @SuppressLint("LogNotTimber")
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                workRunnable?.let { handler.removeCallbacks(it) }
                workRunnable = Runnable {
                    val searchKey = p0.toString()
                    if(searchKey.length>=3) {
                        menuSearch(page, limit, lang, searchKey)
                    }
                    Log.d(TAG, "searchKey: $searchKey")
                }
                handler.postDelayed(workRunnable!!, 500L)
            }
        })

        binding.searchBtn.setOnClickListener {
            hideKeyboard()
            val searchKey = binding.suggestiveSearchET.text.toString()
            if(searchKey.length>=3) {
                menuSearch(page, limit, lang, searchKey)
            }else{
                Toast.makeText(this, getString(R.string.search_expected_toast), Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }


}