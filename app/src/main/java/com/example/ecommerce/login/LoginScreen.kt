package com.example.ecommerce.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.*
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.ecommerce.R.color
import com.example.ecommerce.R.dimen
import com.example.ecommerce.R.drawable
import com.example.ecommerce.R.integer
import com.example.ecommerce.R.string
import com.sahu.gridconfiguration.columnedWidth

@Composable
fun ShowLoginScreen(
    modifier: Modifier = Modifier,
    userName: State<TextFieldValue>,
    userNameValueChange: (TextFieldValue) -> Unit,
    password: State<TextFieldValue>,
    passwordValueChange: (TextFieldValue) -> Unit,
    loginOnClick: () -> Unit,
    forgotOnclick: () -> Unit
) {
    LoginScreenUI(
        modifier = modifier,
        userName = userName,
        userNameValueChange = userNameValueChange,
        password = password,
        passwordValueChange = passwordValueChange,
        loginOnClick = loginOnClick,
        forgotOnclick = forgotOnclick
    )
}

@Composable
private fun LoginScreenUI(
    modifier: Modifier = Modifier,
    userName: State<TextFieldValue>,
    userNameValueChange: (TextFieldValue) -> Unit,
    password: State<TextFieldValue>,
    passwordValueChange: (TextFieldValue) -> Unit,
    loginOnClick: () -> Unit,
    forgotOnclick: () -> Unit
) {


    var passwordVisibility: Boolean by remember { mutableStateOf(false) }


    ConstraintLayout(
        modifier = modifier
            .fillMaxSize()
    ) {

        val (topText, inputFields, bottomLogin) = createRefs()


        Column(modifier = Modifier.constrainAs(topText) {
            bottom.linkTo(inputFields.top)
            start.linkTo(inputFields.start)
            end.linkTo(inputFields.end)
        }) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally),
                text = stringResource(id = string.login),
                style = MaterialTheme.typography.displayLarge.copy(
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )
            )

        }

        Box(
            modifier = Modifier
                .padding(top = dimensionResource(id = dimen.dimen_10dp))
                .constrainAs(inputFields) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }
                .columnedWidth(integerResource(id = integer.input_fields_column))
                .wrapContentHeight()

        ) {
            Column() {
                TextField(
                    value = userName.value,
                    label = { Text(text = "user name") },
                    onValueChange = userNameValueChange,
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                TextField(
                    value = password.value,
                    label = { Text(text = "passcode") },
                    onValueChange = passwordValueChange,
                    singleLine = true,
                    visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = dimensionResource(id = dimen.dimen_10dp)),
                    trailingIcon = {
                        IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                            Icon(
                                painter = painterResource(id = if (passwordVisibility) drawable.ic_password_invisible else drawable.ic_password_visible),
                                contentDescription = null
                            )
                        }
                    }
                )
            }
        }

        Column(
            modifier = Modifier
                .padding(top = dimensionResource(id = dimen.dimen_10dp))
                .constrainAs(bottomLogin) {
                    top.linkTo(inputFields.bottom)
                    start.linkTo(inputFields.start)
                    end.linkTo(inputFields.end)
                }
                .columnedWidth(integerResource(id = integer.column_4))
        ) {

            Button(
                onClick = loginOnClick,
                modifier = Modifier
                    .columnedWidth(integerResource(id = integer.login_button_column))
                    .align(Alignment.CenterHorizontally),
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = color.teal_700)),
                shape = RoundedCornerShape(dimensionResource(id = dimen.dimen_4dp))
            ) {
                Text(
                    text = "Login",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = Color.Black,
                        textAlign = TextAlign.Center
                    )
                )
            }
            Text(
                text = stringResource(id = string.forgot_passcode),
                style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.error),
                modifier = Modifier
                    .padding(top = dimensionResource(id = dimen.dimen_5dp))
                    .align(Alignment.CenterHorizontally)
                    .clickable { forgotOnclick.invoke() })
        }

    }


}