package com.mocat.airpassengeraid.ui.home.home_sub_menu

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.text.LineBreaker.JUSTIFICATION_MODE_INTER_WORD
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.mocat.airpassengeraid.MainActivity
import com.mocat.airpassengeraid.R
import com.mocat.airpassengeraid.api.model.favourite.Wish
import com.mocat.airpassengeraid.api.model.home.Menu
import com.mocat.airpassengeraid.api.model.wishlist.WishSubmitRequest
import com.mocat.airpassengeraid.databinding.FragmentMenuDetailsBinding
import com.mocat.airpassengeraid.ui.home.HomeViewModel
import com.mocat.airpassengeraid.utils.toast
import org.koin.android.ext.android.inject
import timber.log.Timber

class MenuDetailsFragment : Fragment() {
    private var binding: FragmentMenuDetailsBinding? = null
    private var model: Menu? = null
    private var modelWishList: Wish? = null
    private val viewModel: HomeViewModel by inject()
    private var page: Int = 1
    private var limit: Int = 20
    private var lang: String = "bn"
    private var wishFlag: String = "no"
    private var dynamicTitle: String = ""

    private var isWishListedStatusCheck: Boolean = false
    private var isWishListedStatus: Int = 0
    private var articleId_: Int = 0
    private var deviceUniqueId: String = ""
    private var favouriteCount: Int = 0


    private val options = RequestOptions()
        .placeholder(R.drawable.civil_aviation_logo)
        .error(R.drawable.civil_aviation_logo)
    private val imageUrl = "http://15.206.81.173:3008/"


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        (activity as MainActivity?)?.setActionBarTitle("Details Page")
        return FragmentMenuDetailsBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }


    @SuppressLint("HardwareIds")
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        invisible()
        getSubMenuData()
        getWishListData()

        if (wishFlag == "yes") {
            getWishDetails()
        } else {
            getMenuDetails(page, limit, lang, model?.id)
        }
        initClickLister()
        //wishListedStatusCheck()

        //Text justificationMode for align/formatted text start & end properly !!
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            binding?.favouriteContentDetails?.justificationMode = JUSTIFICATION_MODE_INTER_WORD
        }

        //Progressbar visibility Check
       /* if (binding?.favouriteContentTitle?.text != null){
            binding?.progressBar?.visibility = View.VISIBLE
        }
*/
        //App Unique Id Generate here and pass it to wishlist api for set a wishlist according to Unique App Id
        deviceUniqueId = Settings.Secure.getString(requireActivity().contentResolver, Settings.Secure.ANDROID_ID)
        //Toast.makeText(requireContext(), "DeId: $deviceUniqueId", Toast.LENGTH_LONG).show()

        /*viewModel.isWishListed.observe(viewLifecycleOwner) { isWishListed ->
            updateWishImage(isWishListed)
        }*/
    }


    //Set Toolbar Title Dynamically as per navigated Recycler Item Name
    override fun onResume() {
        if (model?.child?.isEmpty() == true && model?.categoryDetails?.isNotEmpty() == true) {
            dynamicTitle = model?.categoryDetails?.first()?.name.toString()
        } else if (modelWishList?.articleInfo?.sections?.first()?.details?.isNotEmpty() == true) {
            dynamicTitle = modelWishList!!.articleInfo.sections.first().details.first().title
        } else {
            dynamicTitle = getString(R.string.details_page_title)
        }
        /* else if (model?.child?.isNotEmpty() == true){
             (activity as MainActivity?)?.setActionBarTitle(getString(R.string.toolbar_title_feedback))
             //dynamicTitle = model?.child?.first()?.childCategoryDetails?.first()?.name.toString()
                 //Toast.makeText(activity, dynamicTitle, Toast.LENGTH_SHORT).show()
         }*/
        (activity as MainActivity?)?.setActionBarTitle(dynamicTitle)
        super.onResume()
    }


    //function ClickListener
    private fun initClickLister() {
        binding?.clickFavouriteIcon?.setOnClickListener {
            favouriteCount++
            if (favouriteCount % 2 == 0) {
                binding?.clickFavouriteIcon?.setImageResource(R.drawable.favorite)
            } else {
                binding?.clickFavouriteIcon?.setImageResource(R.drawable.favourite)
                updateWishList()
                //Toast.makeText(activity, "$articleId_ = $articleId_", Toast.LENGTH_SHORT).show()

            }
        }

        binding?.iconShare?.setOnClickListener {
            context?.let { it1 -> shareMyApp(it1) }
        }

    }

    //function for update wishList API - which Article actually wishes from here details page
    private fun updateWishList() {
        if (binding?.clickFavouriteIcon?.equals(R.drawable.favorite) == true) {
            isWishListedStatus
        } else {
            isWishListedStatus = 1
        }
        val requestBody = WishSubmitRequest(isWishListedStatus, articleId_, deviceUniqueId)
        viewModel.updateWishList(requestBody).observe(viewLifecycleOwner) { response ->
            if (response.status == 200) {
                context?.toast(response.message)
            } else {
                context?.toast(response.message)
            }
        }
    }

    private fun wishListedStatusCheck() {
        binding?.clickFavouriteIcon?.setImageResource(
            if (isWishListedStatus == 0) {
                R.drawable.favorite
            } else R.drawable.favourite
        )
    }


    // function for show Wish Details as per navigate from WishList
    private fun getWishDetails() {

        if (modelWishList?.articleId != null) {
            articleId_ = modelWishList!!.articleId
        }
        if (modelWishList?.articleInfo != null) {
            binding?.favouriteContentTitle?.isVisible = true
            binding?.favouriteContentDetails?.isVisible = true
            binding?.separator?.isVisible = true
            binding?.iconShare?.isVisible = true
            binding?.clickFavouriteIcon?.isVisible = true
            binding?.progressBar?.visibility = View.GONE

            binding?.favouriteContentTitle?.text = modelWishList!!.articleInfo.sections.first().details.first().title
            binding?.favouriteContentDetails?.text = modelWishList!!.articleInfo.sections.first().details.first().description

            if (modelWishList?.articleInfo?.media?.isNotEmpty()!!) {
                Glide.with(binding!!.favouriteContentImage)
                    .load(imageUrl + modelWishList!!.articleInfo.media.first().mediaInfo.source)
                    .apply(options)
                    .into(binding!!.favouriteContentImage)
            }
        } else {
            binding?.emptyView?.visibility = View.VISIBLE
        }
    }


    private fun getMenuDetails(page: Int, limit: Int, lang: String, categoryId: Int?) {

        viewModel.getMenuDetails(page, limit, lang, categoryId)
            .observe(viewLifecycleOwner) { list ->
                val dataList = list.size
                Timber.d("section: $dataList")
                articleId_ = list.first().section.articleId

                if (list.isNotEmpty()) {
                    binding?.favouriteContentTitle?.isVisible = true
                    binding?.favouriteContentDetails?.isVisible = true
                    binding?.separator?.isVisible = true
                    binding?.iconShare?.isVisible = true
                    binding?.clickFavouriteIcon?.isVisible = true

                    binding?.progressBar?.visibility = View.GONE
                    binding?.favouriteContentTitle?.text = list.first().section.details.first().title
                    binding?.favouriteContentDetails?.text = list.first().section.details.first().description

                    if (list.first().media.isNotEmpty()) {
                        Glide.with(binding!!.favouriteContentImage)
                            .load(imageUrl + list.first().media.first().mediaInfo.source)
                            .apply(options)
                            .into(binding!!.favouriteContentImage)
                    }
                } else {
                    binding?.emptyView?.visibility = View.VISIBLE
                }

            }
    }


    //initially all views are Invisible  And after Data Loaded views will be Visible
    private fun invisible() {
        binding?.favouriteContentTitle?.isVisible = false
        binding?.favouriteContentDetails?.isVisible = false
        binding?.separator?.isVisible = false
        binding?.iconShare?.isVisible = false
        binding?.clickFavouriteIcon?.isVisible = false

    }

    //here received bundle model from HomeSubMenuFragment
    private fun getSubMenuData() {
        val bundle: Bundle? = arguments
        bundle?.let {
            this.model = it.getParcelable("model")
            Timber.d("bundleModel $model")
        }

    }

    //here received bundle model from FavouriteListFragment for passing Article Id to get the Details as per as WishList Details from MenuDetails API
    private fun getWishListData() {
        val bundle: Bundle? = arguments
        bundle?.let {
            this.modelWishList = it.getParcelable("wishList")
            Timber.d("wishList $modelWishList")
            if (wishFlag.isNotEmpty()) {
                this.wishFlag = it.getString("wishFlag").toString()
                Timber.d("wishFlag $wishFlag")
            }

        }

    }

    //function for share the app to others
    private fun shareMyApp(context: Context) {
        val appName = context.getString(R.string.app_name)
        val appPlayStoreLink =
            "https://play.google.com/store/apps/details?id=com.mocat.airpassengeraid"

        val sendIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TITLE, appName)
            putExtra(Intent.EXTRA_TEXT, appPlayStoreLink)
        }
        context.startActivity(Intent.createChooser(sendIntent, null))
    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}