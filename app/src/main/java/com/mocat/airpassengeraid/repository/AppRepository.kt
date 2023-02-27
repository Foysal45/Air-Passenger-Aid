package com.mocat.airpassengeraid.repository

import com.mocat.airpassengeraid.api.endpoint.ApiInterface
import com.mocat.airpassengeraid.api.model.feedback.request_body.FeedbackSubmitRequest
import com.mocat.airpassengeraid.api.model.notification.FcmTokenRequest
import com.mocat.airpassengeraid.api.model.wishlist.WishSubmitRequest

class AppRepository(
    private val apiInterface: ApiInterface
) {

   suspend fun getHomeContentList(page: Int, limit: Int, lang: String, parentId: Int?) = apiInterface.getHomeContentList(page, limit, lang, parentId)

   suspend fun getMenuDetails(page: Int, limit: Int, lang: String, categoryId: Int?) = apiInterface.getMenuDetails(page, limit, lang, categoryId)

   suspend fun menuSearch(page: Int, limit: Int, lang: String, title: String) = apiInterface.menuSearch(page, limit, lang, title)

   suspend fun getFavouriteArticleList(deviceId: String, lang: String) = apiInterface.getFavouriteArticleList(deviceId, lang)

   suspend fun getWifiInfoList(lang: String) = apiInterface.getWifiInfoList(lang)

   suspend fun getImportantContactList(lang: String) = apiInterface.getImportantContactList(lang)

   suspend fun getFeedBackType(lang: String) = apiInterface.getFeedBackType(lang)

   suspend fun getNotificationList( is_read: Int, fcm_key: String) = apiInterface.getNotificationList(is_read, fcm_key)

   suspend fun getAirportMap() = apiInterface.getAirportMap()

    suspend fun submitFeedbackData(requestBody: FeedbackSubmitRequest) = apiInterface.submitFeedbackData(requestBody)

    suspend fun updateWishList(requestBody: WishSubmitRequest) = apiInterface.updateWishList(requestBody)

    suspend fun passFcmToken(requestBody: FcmTokenRequest) = apiInterface.passFcmToken(requestBody)

}