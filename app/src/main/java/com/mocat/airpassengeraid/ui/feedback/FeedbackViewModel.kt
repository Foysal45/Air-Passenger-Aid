package com.mocat.airpassengeraid.ui.feedback

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mocat.airpassengeraid.repository.AppRepository
import com.mocat.airpassengeraid.utils.ViewState
import com.haroldadmin.cnradapter.NetworkResponse
import com.mocat.airpassengeraid.api.model.feedback.FeedbackType
import com.mocat.airpassengeraid.api.model.feedback.request_body.FeedbackSubmitRequest
import com.mocat.airpassengeraid.api.model.feedback.request_body.FeedbackSubmitResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class FeedbackViewModel(private val repository: AppRepository) : ViewModel() {

   val viewState = MutableLiveData<ViewState>(ViewState.NONE)

   fun getFeedBackType(lang: String): LiveData<List<FeedbackType>> {

      val responseData: MutableLiveData<List<FeedbackType>> = MutableLiveData()
      viewState.value = ViewState.ProgressState(true)
      viewModelScope.launch(Dispatchers.IO) {
         val response = repository.getFeedBackType(lang)
         withContext(Dispatchers.Main) {
            viewState.value = ViewState.ProgressState(false)
            when (response) {
               is NetworkResponse.Success -> {
                  responseData.value = response.body.payload.feedbackType
                  Timber.d("feedbackType:${response.body}")
               }
               is NetworkResponse.ServerError -> {
                  val message = "দুঃখিত, এই মুহূর্তে আমাদের সার্ভার কানেকশনে সমস্যা হচ্ছে, কিছুক্ষণ পর আবার চেষ্টা করুন"
                  viewState.value = ViewState.ShowMessage(message)
               }
               is NetworkResponse.NetworkError -> {
                  val message = "দুঃখিত, এই মুহূর্তে আপনার ইন্টারনেট কানেকশনে সমস্যা হচ্ছে"
                  viewState.value = ViewState.ShowMessage(message)
               }
               is NetworkResponse.UnknownError -> {
                  val message = "কোথাও কোনো সমস্যা হচ্ছে, আবার চেষ্টা করুন"
                  viewState.value = ViewState.ShowMessage(message)
                  Timber.d(response.error)
               }
            }
         }
      }
      return responseData
   }

    fun submitFeedbackData(requestBody: FeedbackSubmitRequest): LiveData<FeedbackSubmitResponse> {

        val responseData: MutableLiveData<FeedbackSubmitResponse> = MutableLiveData()

        viewState.value = ViewState.ProgressState(true)
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.submitFeedbackData(requestBody)
            withContext(Dispatchers.Main) {
                viewState.value = ViewState.ProgressState(false)
                when (response) {
                    is NetworkResponse.Success -> {
                        if (response.body != null) {
                            responseData.value = response.body
                           Timber.d("feedbackSubmitResponse:${response.body}")
                        }
                    }
                    is NetworkResponse.ServerError -> {
                        val message = "দুঃখিত, এই মুহূর্তে আমাদের সার্ভার কানেকশনে সমস্যা হচ্ছে, কিছুক্ষণ পর আবার চেষ্টা করুন"
                        viewState.value = ViewState.ShowMessage(message)
                    }
                    is NetworkResponse.NetworkError -> {
                        val message = "দুঃখিত, এই মুহূর্তে আপনার ইন্টারনেট কানেকশনে সমস্যা হচ্ছে"
                        viewState.value = ViewState.ShowMessage(message)
                    }
                    is NetworkResponse.UnknownError -> {
                        val message = "কোথাও কোনো সমস্যা হচ্ছে, আবার চেষ্টা করুন"
                        viewState.value = ViewState.ShowMessage(message)
                        Timber.d(response.error)
                    }
                }
            }
        }
        return responseData
    }
}