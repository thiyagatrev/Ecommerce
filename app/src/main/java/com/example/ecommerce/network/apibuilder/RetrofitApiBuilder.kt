package com.example.ecommerce.network.apibuilder

import com.google.gson.Gson
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

open class RetrofitApiBuilder(
    val retrofitBuilder: Retrofit.Builder,
    val gson: Gson,
    val okhttpClient: OkHttpClient
) {
    inline fun <reified T> build(baseUrl: String): T {
        return retrofitBuilder
            .baseUrl(baseUrl)
            .client(okhttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
            .create(T::class.java)
    }
}
