package com.example.ecommerce.network


import com.example.ecommerce.dashboard.ProductListResponse
import com.example.ecommerce.network.model.ProfileDetails
import retrofit2.Response
import retrofit2.http.*

interface ApiService {


    @GET("bc09a745-4346-4025-9611-9da76366dbbc")
    suspend fun getProductsListAsync(): Response<ProductListResponse>


    @GET("aaf97364-eedc-46a5-8f9e-56eb4b3cedd2")
    suspend fun getProfileDetails(): Response<ProfileDetails>

}