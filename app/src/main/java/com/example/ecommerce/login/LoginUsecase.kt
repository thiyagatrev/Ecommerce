package com.example.ecommerce.login

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface ILoginUseCase {
    suspend fun execute(userName: String, password: String): Boolean
}

class LoginUseCase @Inject constructor(
    private val iAuthRepo: IAuthRepo
) : ILoginUseCase {
    override suspend fun execute(userName: String, password: String): Boolean {
        return withContext(Dispatchers.IO) {

            if (userName.isEmpty()) {
                throw Exception("UserName Cannot be empty")
            }
            if (password.isEmpty()) {
                throw Exception("passcode cannot be empty")
            }

            iAuthRepo.execute(userName, password)


        }

    }

}

interface IAuthRepo {
    suspend fun execute(userName: String, password: String): Boolean
}

class AuthRepo @Inject constructor() : IAuthRepo {
    override suspend fun execute(userName: String, password: String): Boolean {
        return if (userName == "username" && password == "123") {
            true
        } else {
            throw Exception("username and password is incorrect")
        }
    }

}
