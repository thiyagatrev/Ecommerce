package com.example.ecommerce.login

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce.safeLaunch
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(private val iLoginUseCase: ILoginUseCase) : ViewModel() {


    private val _userName = mutableStateOf(TextFieldValue(""))
    val userName: State<TextFieldValue> = _userName

    fun setUserName(textFieldValue: TextFieldValue) {
        _userName.value = textFieldValue
    }


    private val _password = mutableStateOf(TextFieldValue(""))
    val password: State<TextFieldValue> = _password

    fun setPassword(textFieldValue: TextFieldValue) {
        _password.value = textFieldValue
    }


    private val _loginFlow = mutableStateOf<StateHandle<Boolean>>(StateHandle.Idle)
    val loginFlow: State<StateHandle<Boolean>> = _loginFlow

    fun login() {
        viewModelScope.safeLaunch(onError = {
            _loginFlow.value = StateHandle.Error(it)
        }) {
            _loginFlow.value = StateHandle.Loading

            val result = iLoginUseCase.execute(
                userName = userName.value.text,
                password = password.value.text
            )
            _loginFlow.value = StateHandle.Success(result)
        }
    }


    fun forgotPassword() {

    }
}

open class StateHandle<out V> {
    object Idle : StateHandle<Nothing>()
    object Loading : StateHandle<Nothing>()
    data class Success<out T>(val data: T) : StateHandle<T>()
    data class Error(val error: Throwable) : StateHandle<Nothing>()
}
