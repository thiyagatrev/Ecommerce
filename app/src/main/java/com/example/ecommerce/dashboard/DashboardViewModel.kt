package com.example.ecommerce.dashboard

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce.network.model.ProfileDetails
import com.example.ecommerce.safeLaunch
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class DashboardViewModel @Inject constructor(private val dashBoardUseCase: IDashBoardUseCase) :
    ViewModel() {


    private val _products = mutableStateOf(ProductInfo())
    val productList: State<ProductInfo> = _products

    private val _profileDetails = mutableStateOf(ProfileDetails())
    val profileDetails: State<ProfileDetails?> = _profileDetails


    init {
        viewModelScope.safeLaunch {
            val response = dashBoardUseCase.getProductsList()
            requireNotNull(response.data)
            _products.value = response.data!!
        }
        viewModelScope.safeLaunch {
            val response = dashBoardUseCase.getProfileDetails()
            requireNotNull(response)
            _profileDetails.value = response

        }
    }


}