package com.mocat.airpassengeraid.ui.feedback

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.Toast
import androidx.core.view.isEmpty
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.mocat.airpassengeraid.R
import com.mocat.airpassengeraid.api.model.feedback.request_body.FeedbackSubmitRequest
import com.mocat.airpassengeraid.databinding.FragmentFeedbackBinding
import com.mocat.airpassengeraid.utils.*
import org.koin.android.ext.android.inject


class FeedbackFragment : Fragment() {

    private var binding: FragmentFeedbackBinding? = null
    private val viewModel: FeedbackViewModel by inject()
    private var lang: String = "bn"
    private val mToastDuration = 100
    private var selectFeedbackType = 0
    private var selectedFeedback = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return FragmentFeedbackBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        getFeedBackType(lang)
        submitFeedback()

        //initially cursor Focus & visible at Name Input Field
        binding?.userNameInput?.let { requestFocus(it) }
        binding?.userNameInput?.isCursorVisible=true
    }

    private fun getFeedBackType(lang: String){

        viewModel.getFeedBackType(lang).observe(viewLifecycleOwner) { list ->
            //val dataList=list.size
            //Timber.d("size: $dataList")
            //Toast.makeText(activity, "Total List = $dataList", Toast.LENGTH_SHORT).show()
            val spinnerAdapter = CustomSpinnerAdapter(requireContext(), R.layout.item_view_spinner_item, list)
            binding?.spinnerFeedbackType?.adapter = spinnerAdapter
            binding?.spinnerFeedbackType?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    selectFeedbackType = position
                    selectedFeedback = list[position].feedbackTypeDetails.first().feedbackTypeId.toString()
                    //Toast.makeText(activity, "selected = $selectedFeedback", Toast.LENGTH_SHORT).show()

                }
            }

        }
    }


    //function for submit user Feedback
    private fun submitFeedback() {
        addTextChangedListener()
        binding?.submitFeedbackBtn?.setOnClickListener {
            binding!!.layoutFeedback.hideKeyboard()
            d("Validation check 1  ${validateFeedbackUserName()}")
            if (validateFeedbackUserName()) {
                d("Validation check 2  ${validateFeedbackUserEmail()}")
                if (validateFeedbackUserEmail()) {
                    d("Validation check 3  ${validateFeedbackUserMobile()}")
                    if (validateFeedbackUserMobile()) {
                        d("Validation check 4  ${validateFeedbackUserText()}")
                        if (validateFeedbackUserText()) {
                            validationCheckForFeedBackTypeSpinner()
                            addTextChangedListener()
                        }
                    }
                }

            }

        }
    }

    private fun submitFeedbackData(){

        val requestBody = FeedbackSubmitRequest(
            binding?.userEmailInput?.text?.trim().toString(),
            binding?.userFeedbackInput?.text?.trim().toString(),
            selectedFeedback.toInt(),
            binding?.userNameInput?.text?.trim().toString(),
            binding?.userPhoneNumberInput?.text?.trim().toString()

        )
        viewModel.submitFeedbackData(requestBody).observe(viewLifecycleOwner) { response->
            if (response.status == 201){
                context?.toast(response.message)
                clearInputFieldData()

            }else{
                context?.toast(response.message)
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
                        binding?.progressBarSubmitFeedback?.visibility = View.VISIBLE
                    } else {
                        binding?.progressBarSubmitFeedback?.visibility = View.GONE
                    }
                }
                else -> {}
            }
        }

    }

    //function for clear InputField after submitted UserFeedback
    private fun clearInputFieldData(){
        binding?.userNameInput?.clearText()
        binding?.userNameInput?.let { requestFocus(it) }
        binding?.userNameLayout?.isErrorEnabled = false
        binding?.userEmailInput?.clearText()
        binding?.userEmailLayout?.isErrorEnabled = false
        binding?.userPhoneNumberInput?.clearText()
        binding?.userPhoneNumberLayout?.isErrorEnabled = false
        binding?.userFeedbackInput?.clearText()
        binding?.userFeedbackLayout?.isErrorEnabled = false
    }

    private fun validationCheckForFeedBackTypeSpinner() {
        val feedbackValidationToast =  Toast.makeText(context, resources.getString(R.string.select_feedback_type), Toast.LENGTH_SHORT)
        if (binding?.spinnerFeedbackType?.isEmpty()!!) {
            displayToast(feedbackValidationToast)
        }else{
            submitFeedbackData()
        }
    }

    private fun validateFeedbackUserName(): Boolean {
        if (binding?.userNameInput.getString().trim().isEmpty()) {
            binding?.userNameLayout?.isErrorEnabled = true
            binding?.userNameLayout?.error = resources.getString(R.string.field_empty_error_message_common)
            binding?.userNameInput?.let { requestFocus(it) }
            return false
        } else {
            binding?.userNameLayout?.isErrorEnabled = false
            return true
        }

    }

    private fun validateFeedbackUserEmail(): Boolean {
        val emailValidationToast =  Toast.makeText(context, resources.getString(R.string.write_correct_email), Toast.LENGTH_SHORT)
        if (binding?.userEmailInput.getString().trim().isEmpty()) {
            binding?.userEmailLayout?.isErrorEnabled = true
            binding?.userEmailLayout?.error = resources.getString(R.string.field_empty_error_message_common)
            binding?.userEmailInput?.let { requestFocus(it) }
            return false
        }else if (!Validator.isValidEmail(binding?.userEmailInput?.text.toString())) {
            displayToast(emailValidationToast)
            return false
        }
        else {
            binding?.userEmailLayout?.isErrorEnabled = false
            return true
        }

    }

    @SuppressLint("ShowToast")
    private fun validateFeedbackUserMobile(): Boolean {
        val phoneValidationToast = Toast.makeText(context, resources.getString(R.string.write_correct_mobile_number), Toast.LENGTH_LONG)
        if (binding?.userPhoneNumberInput.getString().trim().isEmpty()) {
            binding?.userPhoneNumberLayout?.isErrorEnabled = true
            binding?.userPhoneNumberLayout?.error = resources.getString(R.string.field_empty_error_message_common)
            binding?.userPhoneNumberInput?.let { requestFocus(it) }
            return false
        } else if (!Validator.isValidMobileNumber(binding?.userPhoneNumberInput?.text.toString()) || binding?.userPhoneNumberInput?.text.toString().length < 11) {
            displayToast(phoneValidationToast)
            return false
        }
        else {
            binding?.userPhoneNumberLayout?.isErrorEnabled = false
            return true
        }


    }

    private fun validateFeedbackUserText(): Boolean {

         if (binding?.userFeedbackInput.getString().trim().isEmpty()) {
            binding?.userFeedbackLayout?.isErrorEnabled = true
            binding?.userFeedbackLayout?.error = resources.getString(R.string.field_empty_error_message_common)
            binding?.userFeedbackInput?.let { requestFocus(it) }
           return false
        } else {
            binding?.userFeedbackLayout?.isErrorEnabled = false
           return true
        }

    }

    private fun requestFocus(view: View) {
        if (view.requestFocus()) {
            activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
        }
    }

    private fun addTextChangedListener() {
        binding?.userNameInput?.let { addTextChangedListener(it, binding?.userNameLayout!!) }
        binding?.userNameInput?.addTextChangedListener(TW.CrossIconBehave(binding?.userNameInput!!))

        binding?.userEmailInput?.let { addTextChangedListener(it, binding?.userEmailLayout!!) }
        binding?.userEmailInput?.addTextChangedListener(TW.CrossIconBehave(binding?.userEmailInput!!))

        binding?.userPhoneNumberInput?.let { addTextChangedListener(it, binding?.userPhoneNumberLayout!!) }
        binding?.userPhoneNumberInput?.addTextChangedListener(TW.CrossIconBehave(binding?.userPhoneNumberInput!!))

        binding?.userFeedbackInput?.let { addTextChangedListener(it, binding?.userFeedbackLayout!!) }
        binding?.userFeedbackInput?.addTextChangedListener(TW.CrossIconBehave(binding?.userFeedbackInput!!))

    }

    private fun addTextChangedListener(editText: TextInputEditText, inputLayout: TextInputLayout) {
        editText.easyOnTextChangedListener { charSequence ->
            inputValidation(charSequence.toString(), editText, inputLayout)
        }

    }

    // Function to invoke Toast
    private fun displayToast(toast: Toast){
        Thread{
            for(i in 1..mToastDuration/100){
                toast.show()
                Thread.sleep(2000)
                toast.cancel()
            }
        }.start()

    }

        override fun onDestroyView() {
            super.onDestroyView()
            binding = null
        }

}

