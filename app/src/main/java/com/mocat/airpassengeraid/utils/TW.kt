package com.mocat.airpassengeraid.utils

import android.text.Editable
import android.text.TextWatcher
import android.widget.AutoCompleteTextView
import android.widget.EditText
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.mocat.airpassengeraid.R

object TW {

    class CrossIconBehave(private val editText: EditText) : TextWatcher {

        override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

        override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

        override fun afterTextChanged(editable: Editable) {
            showHideCrossButton(editText)
            debug(editText.getString())
        }
    }

    fun Any.debug(message: String) {
        //Log.d(this::class.java.simpleName, message)
        //val crashlytics = FirebaseCrashlytics.getInstance()
        //crashlytics.log(Log.DEBUG, this::class.java.simpleName, message)
    }

    class CrossIconBehaveACTV(private val editText: AutoCompleteTextView) : TextWatcher {

        override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

        override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

        override fun afterTextChanged(editable: Editable) {
            showHideCrossButtonACTV(editText)
            debug(editText.getString())
        }
    }

    private fun showHideCrossButton(editText: EditText) {
        if (editText.text.isNullOrBlank()) {
            editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
        } else {
            editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_close_ash, 0)
            editText.clearTextOnDrawableRightClick()
//            editText.requestFocus()
        }
    }

    private fun showHideCrossButtonACTV(editText: AutoCompleteTextView) {
        if (editText.text.isNullOrBlank()) {
            editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
        } else {
            editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_close_ash, 0)
            editText.clearTextOnDrawableRightClick()
        }
    }
}