package com.mocat.airpassengeraid.api.endpoint

import com.mocat.airpassengeraid.api.model.ErrorResponse
import com.haroldadmin.cnradapter.NetworkResponse
import com.mocat.airpassengeraid.api.model.airport_map.MapBaseModel
import com.mocat.airpassengeraid.api.model.contact_information.ContactBaseModel
import com.mocat.airpassengeraid.api.model.favourite.WishListBaseModel
import com.mocat.airpassengeraid.api.model.feedback.FeedbackBaseModel
import com.mocat.airpassengeraid.api.model.feedback.request_body.FeedbackSubmitRequest
import com.mocat.airpassengeraid.api.model.feedback.request_body.FeedbackSubmitResponse
import com.mocat.airpassengeraid.api.model.home.BaseModel
import com.mocat.airpassengeraid.api.model.home.details.DetailsBase
import com.mocat.airpassengeraid.api.model.notification.FcmTokenRequest
import com.mocat.airpassengeraid.api.model.notification.getNotificationList.NotificationBaseModel
import com.mocat.airpassengeraid.api.model.wifi_info.WifiBaseModel
import com.mocat.airpassengeraid.api.model.wishlist.WishSubmitRequest
import com.mocat.airpassengeraid.api.model.wishlist.WishSubmitResponse
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiInterface {

    companion object {
        operator fun invoke(retrofit: Retrofit): ApiInterface {
            return retrofit.create(ApiInterface::class.java)
        }
    }


    @GET("menus")
    suspend fun getHomeContentList(@Query("page") page: Int, @Query("limit") limit: Int, @Query("lang") lang: String, @Query("parentId") parentId: Int?) : NetworkResponse<BaseModel, ErrorResponse>

    @GET("sections")
    suspend fun getMenuDetails(@Query("page") page: Int, @Query("limit") limit: Int, @Query("lang") lang: String,  @Query("categoryId") categoryId: Int?) : NetworkResponse<DetailsBase, ErrorResponse>

    @GET("sections")
    suspend fun menuSearch(@Query("page") page: Int, @Query("limit") limit: Int, @Query("lang") lang: String,  @Query("title") title: String) : NetworkResponse<DetailsBase, ErrorResponse>

    @GET("public/wifi")
    suspend fun getWifiInfoList( @Query("lang") lang: String) : NetworkResponse<WifiBaseModel, ErrorResponse>

    @GET("public/contacts")
    suspend fun getImportantContactList(@Query("lang") lang: String) : NetworkResponse<ContactBaseModel, ErrorResponse>

    @GET("public/feedback-type")
    suspend fun getFeedBackType( @Query("lang") lang: String) : NetworkResponse<FeedbackBaseModel, ErrorResponse>

    @GET("fcm/notifications")
    suspend fun getNotificationList(@Query("is_read") is_read: Int, @Query("fcm_key") lang: String) : NetworkResponse<NotificationBaseModel, ErrorResponse>

    @GET("public/maps/list")
    suspend fun getAirportMap() : NetworkResponse<MapBaseModel, ErrorResponse>

    @GET("sections/wish")
    suspend fun getFavouriteArticleList(@Query("deviceId") deviceId: String, @Query("lang") lang: String) : NetworkResponse<WishListBaseModel, ErrorResponse>


    @POST("public/feedback-responses")
    suspend fun submitFeedbackData(@Body requestBody: FeedbackSubmitRequest): NetworkResponse<FeedbackSubmitResponse, ErrorResponse>

    @POST("sections/{articleId}/wish")
    suspend fun updateWishList(@Body requestBody: WishSubmitRequest): NetworkResponse<WishSubmitResponse, ErrorResponse>

    @POST("fcm")
    suspend fun passFcmToken(@Body requestBody: FcmTokenRequest): NetworkResponse<String, ErrorResponse>


}