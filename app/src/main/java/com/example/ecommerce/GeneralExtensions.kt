package com.example.ecommerce

import android.content.Context
import android.widget.Toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

val Any.TAG: String
    get() {
        return if (!javaClass.isAnonymousClass) {
            val name = javaClass.simpleName
            if (name.length <= 23) name else name.substring(0, 23)// first 23 chars
        } else {
            val name = javaClass.name
            if (name.length <= 23) name else name.substring(
                name.length - 23,
                name.length
            )// last 23 chars
        }
    }

inline fun Context.checkInternetAndExecute(showToast: Boolean = true, function: () -> Unit) {
    if (ECommerceApplication.isNetConnected())
        function()
    else if (showToast)
        showToast(getString(R.string.msg_no_internet), this)
}

fun showToast(text: String?, context: Context) {
    GlobalScope.launch(Dispatchers.Main) {
        text?.let {
            Toast.makeText(context, text, Toast.LENGTH_LONG).show()
        }
    }
}

fun showToast(text: String?) {
    GlobalScope.launch(Dispatchers.Main) {
        text?.let {
            Toast.makeText(ECommerceApplication.appContext, text, Toast.LENGTH_LONG).show()
        }
    }
}

inline fun Long.toDateHoursMinutesSeconds(): String {
    val sdf = SimpleDateFormat("MMMM dd,hh:mm:ss")
    val netDate = Date(this)
    return sdf.format(netDate)
}