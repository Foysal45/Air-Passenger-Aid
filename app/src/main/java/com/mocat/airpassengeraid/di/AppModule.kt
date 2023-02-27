package com.mocat.airpassengeraid.di

import com.mocat.airpassengeraid.api.endpoint.ApiInterface
import com.mocat.airpassengeraid.repository.AppRepository
import com.mocat.airpassengeraid.ui.home.HomeViewModel
import com.mocat.airpassengeraid.api.baseUrl.AppConstant
import com.mocat.airpassengeraid.ui.airport_map.AirportMapViewModel
import com.mocat.airpassengeraid.ui.contact_information.ImportantContactViewModel
import com.mocat.airpassengeraid.ui.favourite.FavouriteContentListViewModel
import com.mocat.airpassengeraid.ui.feedback.FeedbackViewModel
import com.mocat.airpassengeraid.ui.notification.NotificationViewModel
import com.mocat.airpassengeraid.ui.wifi_info.WifiInfoViewModel
import com.mocat.airpassengeraid.utils.RetrofitUtils.createCache
import com.mocat.airpassengeraid.utils.RetrofitUtils.createOkHttpClient
import com.mocat.airpassengeraid.utils.RetrofitUtils.getGson
import com.mocat.airpassengeraid.utils.RetrofitUtils.retrofitInstance
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val appModule = module {

    single { getGson() }
    single { createCache(get()) }
    single { createOkHttpClient(get()) }
    single(named("normal")) { createOkHttpClient(get()) }

    single(named("api")) { retrofitInstance(AppConstant.BASE_URL, get(), get()) }

    single { ApiInterface(get(named("api"))) }

    single { AppRepository(get()) }

    viewModel { HomeViewModel(get()) }

    viewModel { WifiInfoViewModel(get()) }

    viewModel { ImportantContactViewModel(get()) }

    viewModel { FeedbackViewModel(get()) }

    viewModel { AirportMapViewModel(get()) }

    viewModel { FavouriteContentListViewModel(get()) }

    viewModel { NotificationViewModel(get()) }


}