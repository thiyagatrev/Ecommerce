package com.example.ecommerce.dashboard

import com.google.gson.annotations.SerializedName


data class ProductListResponse(

    @SerializedName("data") var data: ProductInfo? = ProductInfo()

)

data class ProductInfo(

    @SerializedName("products") var products: ArrayList<Product> = arrayListOf()

)

data class Product(

    @SerializedName("id") var id: String? = null,
    @SerializedName("brand") var brand: String? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("productDesc") var productDesc: String? = null,
    @SerializedName("price") var price: String? = null,
    @SerializedName("offerPrice") var offerPrice: String? = null,
    @SerializedName("productUrl") var productUrl: String? = null

)
