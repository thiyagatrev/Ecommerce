package com.example.ecommerce

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.multidex.MultiDex
import com.facebook.stetho.Stetho
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ECommerceApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        appContext = this.applicationContext
        Prefs.createPrefs(appContext)
        Stetho.initializeWithDefaults(this)
        MultiDex.install(this)
    }

    companion object {
        lateinit var appContext: Context
        fun isNetConnected(): Boolean {
            val cm =
                appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
            var result = false
            if (activeNetwork != null) {
                result = activeNetwork.isConnectedOrConnecting
            }
            return result
        }
    }
}
