package com.example.ecommerce

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

inline fun CoroutineScope.safeLaunch(
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    crossinline onError: (Exception) -> Unit = { Log.e("coroutineContext", it.toString()) },
    crossinline block: suspend CoroutineScope.() -> Unit,
): Job {
    return launch(context, start) {
        try {
            block()
        } catch (e: Throwable) {
            onError.invoke(Exception(e))
        }
    }
}

@Composable
fun ShowLoadingUi() {

    Box(modifier = Modifier
        .fillMaxSize()
        .pointerInput(Unit) {}
        .background(color = Color.Black.copy(alpha = 0.1F))) {
        val strokeWidth = 5.dp
        CircularProgressIndicator(
            modifier = Modifier
                .align(Alignment.Center)
                .wrapContentHeight(),
            color = colorResource(id = R.color.teal_700),
            strokeWidth = strokeWidth
        )

    }

}

object Prefs {

    const val userName: String = "username"
    const val password: String = "password"
    const val isLoggedIn: String = "isLoggedIn"
    private val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

    private lateinit var prefs: SharedPreferences

    fun createPrefs(context: Context) {
        prefs = EncryptedSharedPreferences.create(
            "encrypted_preferences", // fileName
            masterKeyAlias, // masterKeyAlias
            context, // context
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV, // prefKeyEncryptionScheme
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM // prefvalueEncryptionScheme
        )
    }

    fun setString(name: String, value: String) {
        prefs.edit().putString(name, value).apply()
    }

    fun getString(name: String) = prefs.getString(name, "")

    fun setBool(name: String, value: Boolean) = prefs.edit().putBoolean(name, value).apply()
    fun getBool(name: String) = prefs.getBoolean(name, false)

}