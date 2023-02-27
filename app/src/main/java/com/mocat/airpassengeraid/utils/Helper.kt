package com.mocat.airpassengeraid.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.mocat.airpassengeraid.R

import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.crashlytics.FirebaseCrashlytics

fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

fun Activity.hideKeyboard() {
    hideKeyboard(currentFocus ?: View(this))
}

fun Context.hideKeyboard(view: View) {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Any.d(message: String) {
    //Log.d("+++" + this::class.java.simpleName, message)
}

fun EditText?.getString(): String {
    return this!!.text.trim().toString()
}

fun TextInputLayout.showError(errorMessage: String?) {
    isErrorEnabled = true
    error = errorMessage
}

fun TextView.easyOnTextChangedListener(listener: (e: CharSequence) -> Unit) =
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(p0: Editable) {
        }

        override fun beforeTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {
            listener(p0)
        }
    })
fun validateField(char: String, et: TextInputEditText, til: TextInputLayout): Boolean{
    return TextUtils.isEmpty(char)
}

fun inputValidation(char: String, et: TextInputEditText, til: TextInputLayout): Boolean {
    when {
        TextUtils.isEmpty(char) -> {
            til.showError("This Field can not be empty")
            return false
        }
        char.isEmpty() -> {
            et.requestFocus()
            return false
        }
        else -> til.hideError()
    }
    return true
}

fun EditText.clearText() {
    text?.clear()
}
@SuppressLint("LogNotTimber")
fun Any.logException(e: java.lang.Exception) {
    val crashlytics = FirebaseCrashlytics.getInstance()
    //crashlytics.logException(e)
    Log.e(this::class.java.simpleName, e.toString())
}

@SuppressLint("ClickableViewAccessibility")
fun EditText.clearTextOnDrawableRightClick() {
    setOnTouchListener(View.OnTouchListener { v, event ->
        val DRAWABLE_LEFT = 0
        val DRAWABLE_TOP = 1
        val DRAWABLE_RIGHT = 2
        val DRAWABLE_BOTTOM = 3
        try {
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= right - compoundDrawables[DRAWABLE_RIGHT]?.bounds?.width()!!) {
                    clearText()
                    return@OnTouchListener false
                }
            }
        } catch (e: Exception) {
            logException(e)
            return@OnTouchListener false
        }
        false
    })
}

fun isValidate(
    etCurrent: TextInputEditText?, tilCurrent: TextInputLayout?, etNext: TextInputEditText?, last: Boolean, validation: Int
): Int {
    var valid: Int = validation
    if (last) {
        if (TextUtils.isEmpty(etCurrent?.getString())) {
            tilCurrent?.showError("This field can not be empty")
        } else {
            valid++
            tilCurrent?.isErrorEnabled = false
            etNext?.requestFocus()
        }
    } else {
        if (TextUtils.isEmpty(etCurrent?.getString())) {
            tilCurrent?.showError("This field can not be empty")
            etCurrent?.requestFocus()
        }
        etNext?.requestFocus()
        tilCurrent?.hideError()
    }
    return valid
}
fun TextInputLayout.hideError() {
    isErrorEnabled = false
}


fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

@SuppressLint("SuspiciousIndentation")
fun Context.toast(msg: String?, time: Int = Toast.LENGTH_SHORT) {
    if (!msg.isNullOrEmpty())
    Toast.makeText(this, msg, time).show()
}

fun Fragment.alert(title: CharSequence? = null, message: CharSequence? = null, showCancel: Boolean = false, positiveButtonText: String = "ঠিক আছে", negativeButtonText: String = "ক্যানসেল", listener: ((type: Int) -> Unit)? = null): AlertDialog {

    val builder = MaterialAlertDialogBuilder(requireContext())
    builder.setTitle(title)
    // Display a message on alert dialog
    builder.setMessage(message)
    // Set a positive button and its click listener on alert dialog
    builder.setPositiveButton(positiveButtonText) { dialog, which ->
        dialog.dismiss()
        listener?.invoke(AlertDialog.BUTTON_POSITIVE)
    }
    // Display a negative button on alert dialog
    if (showCancel) {
        builder.setNegativeButton(negativeButtonText) { dialog, which ->
            dialog.dismiss()
            listener?.invoke(AlertDialog.BUTTON_NEGATIVE)
        }
    }

    val dialog = builder.create()
    val typeface = ResourcesCompat.getFont(requireContext(), R.font.solaiman)
    val textView = dialog.findViewById<TextView>(android.R.id.message)
    textView?.typeface = typeface
    return dialog
}

fun Activity.progressDialog(message: String = getString(R.string.wait)): ProgressDialog {

    val dialog = ProgressDialog(this)
    with(dialog) {
        setMessage(message)
    }
    return dialog
}


fun Fragment.callHelplineNumber(number: String) {
    try {
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$number"))
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    } catch (e: Exception) {
        requireContext().toast("Could not find an activity to place the call")
    }
}


fun Activity.alert(title: CharSequence? = null, message: CharSequence? = null, showCancel: Boolean = false, positiveButtonText: String = "ঠিক আছে", negativeButtonText: String = "ক্যানসেল", listener: ((type: Int) -> Unit)? = null): AlertDialog {

    val builder = MaterialAlertDialogBuilder(this)
    builder.setTitle(title)
    // Display a message on alert dialog
    builder.setMessage(message)
    // Set a positive button and its click listener on alert dialog
    builder.setPositiveButton(positiveButtonText) { dialog, which ->
        dialog.dismiss()
        listener?.invoke(AlertDialog.BUTTON_POSITIVE)
    }
    // Display a negative button on alert dialog
    if (showCancel) {
        builder.setNegativeButton(negativeButtonText) { dialog, which ->
            dialog.dismiss()
            listener?.invoke(AlertDialog.BUTTON_NEGATIVE)
        }
    }

    val dialog = builder.create()
    val typeface = ResourcesCompat.getFont(this, R.font.solaiman)
    val textView = dialog.findViewById<TextView>(android.R.id.message)
    textView?.typeface = typeface
    return dialog
}


fun View.snackbar(message: String, length: Int = Snackbar.LENGTH_LONG){
    Snackbar.make(this, message, length).also { snackbar ->
        snackbar.setAction("Cancel") {
            snackbar.dismiss()
        }
    }.show()
}

fun View.snackbar(message: String, length: Int = Snackbar.LENGTH_INDEFINITE, actionName: String, onClick: ((view: View) -> Unit)? = null): Snackbar {
    return Snackbar.make(this, message, length).also { snackbar ->
        snackbar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text).maxLines = 5
        snackbar.setActionTextColor(Color.YELLOW)
        snackbar.setAction(actionName) {
            onClick?.invoke(it)
            snackbar.dismiss()
        }
    }
}

fun Context.isConnectedToNetwork(): Boolean {
    var isConnected = false
    val connectivityManager = this.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        connectivityManager?.let {
            val networkCapabilities = it.activeNetwork ?: return false
            val activeNetwork = it.getNetworkCapabilities(networkCapabilities) ?: return false
            isConnected = activeNetwork.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            /*activeNetwork.apply {
                isConnected = when{
                    hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    else -> false
                }
            }*/
        }
    } else {
        isConnected = connectivityManager?.activeNetworkInfo?.isConnectedOrConnecting == true
    }
    return isConnected
}

fun Fragment.progressDialog(message: String = getString(R.string.wait)): ProgressDialog {

    val dialog = ProgressDialog(requireContext())
    with(dialog) {
        setMessage(message)
    }
    return dialog
}

val <T> T.exhaustive: T
    get() = this

fun Number.spToPx(context: Context) = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_SP, this.toFloat(), context.resources.displayMetrics).toInt()

fun isPackageInstalled(packageManager: PackageManager, packageName: String): Boolean {
    return try {
        packageManager.getPackageInfo(packageName, 0)
        true
    } catch (e: Exception) {
        false
    }
}

fun isValidCoordinate(coordinate: String?): Boolean {
    if (coordinate.isNullOrEmpty()) return false
    if (coordinate.trim().isEmpty()) return false
    if (coordinate == "0.0") return false
    if (coordinate == "0") return false
    return true
}

fun Bundle.bundleToString(): String {
    return this.keySet().joinToString(", ", "{", "}") { key ->
        "$key=${this[key]}"
    }
}

fun Activity.appVersion(): String {
    return try {
        val pInfo: PackageInfo = packageManager.getPackageInfo(packageName, 0)
        pInfo.versionName
    } catch (e: Exception) {
        e.printStackTrace()
        ""
    }
}

fun Fragment.appVersion(): String {
    return try {
        val pInfo: PackageInfo? = this.context?.packageManager?.getPackageInfo(this.context?.packageName ?: "", 0)
        pInfo?.versionName ?: ""
    } catch (e: Exception) {
        e.printStackTrace()
        ""
    }
}

fun Activity.appVersionCode(): Int {
    return try {
        val pInfo: PackageInfo = packageManager.getPackageInfo(packageName, 0)
        pInfo.versionCode
    } catch (e: Exception) {
        e.printStackTrace()
        0
    }
}

fun Context.dpToPx(value: Float): Int {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, this.resources.displayMetrics).toInt()
}

fun Fragment.appVersionCode(): Int {
    return try {
        val pInfo: PackageInfo? = this.context?.packageManager?.getPackageInfo(this.context?.packageName ?: "", 0)
        pInfo?.versionCode ?: 0
    } catch (e: Exception) {
        e.printStackTrace()
        0
    }
}


fun generateNameInitial(name: String?): String {
    if (name.isNullOrEmpty()) return ""
    var initial: String = ""
    val array = name.trim().split(" ")
    array.forEach {
        if (it.isNotEmpty()) {
            initial += it[0]
        }
    }
    return  initial

}