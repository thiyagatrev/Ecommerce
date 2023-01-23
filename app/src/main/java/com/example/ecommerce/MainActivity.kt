package com.example.ecommerce

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.integerResource
import androidx.compose.ui.unit.dp
import com.example.ecommerce.dashboard.ShowDashBoardScreen
import com.example.ecommerce.login.LoginViewModel
import com.example.ecommerce.login.ShowLoginScreen
import com.example.ecommerce.login.StateHandle
import com.example.ecommerce.ui.theme.EcommerceTheme
import com.google.accompanist.pager.ExperimentalPagerApi
import com.sahu.gridconfiguration.GridConfiguration
import com.sahu.gridconfiguration.LocalGridConfiguration
import com.sahu.gridconfiguration.rememberGridConfiguration
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val TAG = "MainActivity"

    private val loginViewModel: LoginViewModel by viewModels()


    @ExperimentalPagerApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EcommerceTheme {
                // A surface container using the 'background' color from the theme
                CompositionLocalProvider(
                    LocalGridConfiguration provides rememberGridConfiguration(),
                ) {
                    Surface(modifier = Modifier.fillMaxSize()) {
                        Content()
                    }
                }
            }
        }
    }

    @ExperimentalPagerApi
    @Composable
    fun Content() {

        val shouldMoveToNextPage = remember { mutableStateOf(ScreenState.NONE) }

        when (shouldMoveToNextPage.value) {
            ScreenState.LOGIN -> {
                ShowLoginScreen(
                    userName = loginViewModel.userName,
                    userNameValueChange = { loginViewModel.setUserName(it) },
                    password = loginViewModel.password,
                    passwordValueChange = { loginViewModel.setPassword(it) },
                    loginOnClick = {
                        Log.e(TAG, "loginOnClick: ")
                        loginViewModel.login()
                    },
                    forgotOnclick = {
                        Log.e(TAG, "forgot password")
                        loginViewModel.forgotPassword()
                    }
                )
            }
            ScreenState.DASHBOARD -> {
                ShowDashBoardScreen()
            }
            ScreenState.NONE -> {
                if (Prefs.getBool(Prefs.isLoggedIn)) {
                    shouldMoveToNextPage.value = ScreenState.DASHBOARD
                } else {
                    shouldMoveToNextPage.value = ScreenState.LOGIN
                }
            }

        }


        when (val value = loginViewModel.loginFlow.value) {
            is StateHandle.Loading -> {
                Log.e(TAG, "loading: ")
                ShowLoadingUi()
            }
            is StateHandle.Success -> {
                Log.e(TAG, "Success: ")
                shouldMoveToNextPage.value = ScreenState.DASHBOARD
                Prefs.setString(Prefs.userName, loginViewModel.userName.value.text)
                Prefs.setString(Prefs.password, loginViewModel.userName.value.text)
                Prefs.setBool(Prefs.isLoggedIn, true)

            }
            is StateHandle.Error -> {
                Log.e(TAG, "Content:" + value.toString())
                ShowErrorUI()
            }
        }

    }


    private fun ShowErrorUI() {
        Toast.makeText(this@MainActivity, "username and password is incorrect", Toast.LENGTH_LONG)
            .show()
    }


}

enum class ScreenState {
    LOGIN,
    DASHBOARD,
    NONE
}

@Composable
fun rememberGridConfiguration(): GridConfiguration =
    rememberGridConfiguration(
        layoutWidth = LocalConfiguration.current.screenWidthDp.dp,
        horizontalMargin = dimensionResource(id = R.dimen.layout_horizontal_margin),
        gutterWidth = dimensionResource(id = R.dimen.layout_gutter_width),
        totalColumns = integerResource(id = R.integer.total_columns)
    )
