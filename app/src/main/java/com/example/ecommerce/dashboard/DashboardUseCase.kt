package com.example.ecommerce.dashboard

import com.example.ecommerce.network.ApiService
import com.example.ecommerce.network.model.ProfileDetails
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


interface IDashBoardUseCase {
    suspend fun getProductsList(): ProductListResponse
    suspend fun getProfileDetails(): ProfileDetails?
}

class DashBoardUseCase @Inject constructor(private val dashBoardRepo: IDashBoardRepo) :
    IDashBoardUseCase {
    override suspend fun getProductsList(): ProductListResponse {
        return withContext(Dispatchers.IO) {
            dashBoardRepo.getProductsList()
        }
    }

    override suspend fun getProfileDetails(): ProfileDetails? {
        return withContext(Dispatchers.IO) {
            dashBoardRepo.getProfileDetails()
        }
    }
}

interface IDashBoardRepo {
    suspend fun getProductsList(): ProductListResponse
    suspend fun getProfileDetails(): ProfileDetails?
}

class DashBoardRepo @Inject constructor(private val apiService: ApiService) : IDashBoardRepo {
    override suspend fun getProductsList(): ProductListResponse {

        return withContext(Dispatchers.IO) {

            val response = apiService.getProductsListAsync()

            if (response.body() != null) {
                response.body()!!
            } else {
                ProductListResponse()
            }


        }
    }

    override suspend fun getProfileDetails(): ProfileDetails? {
        return withContext(Dispatchers.IO) {
            val response = apiService.getProfileDetails()
            response.body()!!
        }
    }

}


