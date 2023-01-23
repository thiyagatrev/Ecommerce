package com.example.ecommerce.login

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface LoginModule {
    @Binds
    fun bindLoginUseCase(loginUseCase: LoginUseCase): ILoginUseCase

    @Binds
    fun bindAuthRepo(authRepo: AuthRepo): IAuthRepo
}