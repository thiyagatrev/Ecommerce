package com.example.ecommerce.dashboard

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
internal interface DashboardModule {
    @Binds
    fun bindDashBoardUseCase(productUseCase: DashBoardUseCase): IDashBoardUseCase

    @Binds
    fun bindProductRepo(productRepo: DashBoardRepo): IDashBoardRepo
}