package com.example.ecommerce.network

import android.util.Log
import com.example.ecommerce.BuildConfig
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
private object ApiModule {

    @Singleton
    @Provides
    fun providesBaseUrl(): String = "https://run.mocky.io/v3/"

    @Singleton
    @Provides
    fun providesRetrofitBuilder(): Retrofit.Builder = Retrofit.Builder()

    @Singleton
    @Provides
    fun providesHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return httpLoggingInterceptor
    }

    @Singleton
    @Provides
    fun providesGson(): Gson = GsonBuilder().create()


    @Singleton
    @Provides
    fun providesNetworkInterceptor(): StethoInterceptor = StethoInterceptor()

    @Singleton
    @Provides
    fun providesOkHttpClientBuilder(): OkHttpClient.Builder = OkHttpClient.Builder()


    @Singleton
    @Provides
    fun providesOkhttpClient(
        okhttpBuilder: OkHttpClient.Builder,
        stethoInterceptor: StethoInterceptor,
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        if (BuildConfig.DEBUG) {
            okhttpBuilder.addInterceptor(loggingInterceptor)
        }
        return okhttpBuilder
            .addInterceptor(stethoInterceptor)
            .addInterceptor { chain ->
                val original: Request = chain.request()
                val request: Request = original.newBuilder()
                    .header("Content-Type", "application/json; charset=utf-8")
                    .build()

                val response: Response = chain.proceed(request)
                when (response.code) {
                    401 -> {
                        Log.e("Unauthorized-->", response.message)
                    }
                    400, 403, 404 -> {
                        Log.e("Error--> ", response.message)
                    }
                    500, 503 -> {
                        Log.e("ServerError-->", response.message)
                    }
                }
                response
            }
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .followRedirects(false).build()
    }


    @Singleton
    @Provides
    fun providesRetrofitWithoutAuthentication(
        baseUrl: String,
        okhttpClient: OkHttpClient,
        gson: Gson,
        retrofitBuilder: Retrofit.Builder
    ): Retrofit {
        return retrofitBuilder
            .baseUrl(baseUrl)
            .client(okhttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
    }


    @Singleton
    @Provides
    fun providesApiCallsWithoutAuthentication(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }


}
