package com.example.ecommerce.dashboard

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.integerResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.ecommerce.R
import com.example.ecommerce.ShowLoadingUi
import com.example.ecommerce.network.model.ProfileDetails
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.sahu.gridconfiguration.LocalGridConfiguration
import com.sahu.gridconfiguration.columnedWidth


@ExperimentalPagerApi
@Composable
fun ShowDashBoardScreen(modifier: Modifier = Modifier) {

    DashboardUI(modifier = modifier)
}

@ExperimentalPagerApi
@Composable
private fun DashboardUI(modifier: Modifier = Modifier) {

    val pagerState = rememberPagerState()

    var shouldShowProductInFullScreen by remember {
        mutableStateOf(Pair(false, Product()))
    }

    BackHandler(true) {
        if (shouldShowProductInFullScreen.first) {
            shouldShowProductInFullScreen = Pair(false, Product())
        }
    }
    Box(modifier = modifier.background(Color.White)) {

        if (shouldShowProductInFullScreen.first) {
            ShowProductInFullScreen(product = shouldShowProductInFullScreen.second)
        } else {

            HorizontalPager(
                userScrollEnabled = false,
                count = 2,
                state = pagerState,
            ) { page ->

                when (page) {
                    0 -> {
                        ShowProductsUI(productImageOnClick = {
                            shouldShowProductInFullScreen = Pair(true, it)
                        })
                    }
                    1 -> {
                        ShowProfilePage()
                    }
                }

            }
            var tabIndex by remember { mutableStateOf(0) }
            val tabData = listOf(
                "Products",
                "Profile"

            )
            LaunchedEffect(key1 = tabIndex, block = {
                pagerState.animateScrollToPage(page = tabIndex)
            })
            TabRow(
                modifier = Modifier.align(Alignment.BottomCenter),
                selectedTabIndex = tabIndex
            ) {
                tabData.forEachIndexed { index, text ->
                    Tab(
                        selected = tabIndex == index,
                        onClick = { tabIndex = index },
                        text = { Text(text = text, style = TextStyle(color = Color.Black)) },
                        selectedContentColor = colorResource(id = R.color.purple_500)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ShowProductInFullScreen(modifier: Modifier = Modifier, product: Product) {

    Box(modifier = modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .columnedWidth(integerResource(id = R.integer.column_9))
        ) {


            GlideImage(
                model = product.productUrl,
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .columnedWidth(integerResource(id = R.integer.image_column_width))
            )

            ProfileTextView(
                title = stringResource(id = R.string.brand),
                value = product.brand.toString(),
                valueStyle = MaterialTheme.typography.displaySmall
            )
            ProfileTextView(
                title = stringResource(id = R.string.name),
                value = product.name.toString(),
                valueStyle = MaterialTheme.typography.displaySmall
            )
            ProfileTextView(
                title = stringResource(id = R.string.product_desc),
                value = product.productDesc.toString(),
                valueStyle = MaterialTheme.typography.headlineMedium
            )
        }

    }


}

@Composable
private fun ShowProductsUI(
    productViewModel: DashboardViewModel = viewModel(),
    productImageOnClick: (product: Product) -> Unit
) {

    val productState = productViewModel.productList

    if (productState.value.products.size == 0) {
        ShowEmptyListView()
    }
    LazyColumn(modifier = Modifier.padding(top = dimensionResource(id = R.dimen.dimen_18dp))) {
        itemsIndexed(items = productState.value.products) { index, item ->

            if (index == 1) {
                ShowRewardProgramUI()
            }
            ShowProductUI(item, productImageOnClick = productImageOnClick)
        }
    }


}


@Composable
fun ShowEmptyListView(modifier: Modifier = Modifier) {

    Box(modifier = modifier.fillMaxSize()) {

        Text(
            modifier = Modifier.align(Alignment.Center),
            text = stringResource(id = R.string.fetching_the_list),
            style = MaterialTheme.typography.bodyLarge.copy(color = colorResource(id = R.color.black))
        )
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ShowProductUI(product: Product, productImageOnClick: (product: Product) -> Unit) {


    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { productImageOnClick.invoke(product) }
            .wrapContentHeight()
            .padding(
                start = LocalGridConfiguration.current.columnWidth,
                end = LocalGridConfiguration.current.columnWidth,
                top = dimensionResource(
                    id = R.dimen.dimen_2dp
                )
            )
            .wrapContentHeight()
    ) {
        val (productPic, productTitle, productDescription, productPrize) = createRefs()

        GlideImage(
            model = product.productUrl,
            contentDescription = null,
            modifier = Modifier
                .columnedWidth(integerResource(id = R.integer.column_3))
                .constrainAs(productPic) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    width = Dimension.fillToConstraints
                }
                .clickable(onClick = { productImageOnClick.invoke(product) }),
        )

        Text(
            modifier = Modifier.constrainAs(productTitle) {
                start.linkTo(productPic.end)
                end.linkTo(parent.end)
                top.linkTo(parent.top)
                width = Dimension.fillToConstraints
            },
            text = product.name.toString(),

            style = MaterialTheme.typography.headlineLarge.copy(
                Color.Black
            )
        )
        Text(
            modifier = Modifier.constrainAs(productDescription) {
                start.linkTo(productPic.end)
                end.linkTo(parent.end)
                top.linkTo(productTitle.bottom)
                width = Dimension.fillToConstraints
            },
            text = product.productDesc.toString(),
            style = MaterialTheme.typography.bodyMedium.copy(
                Color.Black
            )
        )

        Text(
            modifier = Modifier.constrainAs(productPrize) {
                start.linkTo(productPic.end)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
                width = Dimension.fillToConstraints
            },
            text = stringResource(id = R.string.price) + ": " + product.price.toString(),
            style = MaterialTheme.typography.titleMedium.copy(
                color = Color.Black,
                textAlign = TextAlign.End
            )
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.DarkGray)
                .size(dimensionResource(id = R.dimen.dimen_1dp))
        )
    }

}


@Composable
private fun ShowProfilePage(productViewModel: DashboardViewModel = viewModel()) {
    val profileState = productViewModel.profileDetails


    if (profileState.value == ProfileDetails()) {
        ShowLoadingUi()
        return
    }


    Box(modifier = Modifier.fillMaxSize()) {


        Column(
            modifier = Modifier
                .columnedWidth(integerResource(id = R.integer.column_9))
                .align(Alignment.Center)
        ) {

            ProfileTextView(
                modifier = Modifier.padding(top = dimensionResource(id = R.dimen.dimen_10dp)),
                title = stringResource(id = R.string.userName),
                value = profileState.value?.username.toString(),
                valueStyle = MaterialTheme.typography.displaySmall.copy(
                    Color.Black
                )
            )

            ProfileTextView(
                modifier = Modifier.padding(top = dimensionResource(id = R.dimen.dimen_10dp)),
                title = stringResource(id = R.string.name),
                value = profileState.value?.firstname.toString() + " " + profileState.value?.lastName.toString(),
                valueStyle = MaterialTheme.typography.displaySmall.copy(
                    Color.Black
                )
            )

            ProfileTextView(
                modifier = Modifier.padding(top = dimensionResource(id = R.dimen.dimen_10dp)),
                title = stringResource(id = R.string.dob),
                value = profileState.value?.dob.toString(),
                valueStyle = MaterialTheme.typography.displaySmall.copy(
                    Color.Black
                )
            )

            ProfileTextView(
                modifier = Modifier.padding(top = dimensionResource(id = R.dimen.dimen_10dp)),
                title = stringResource(id = R.string.address),
                value = profileState.value?.address.toString(),
                valueStyle = MaterialTheme.typography.titleMedium.copy(
                    Color.Black
                )
            )

            ProfileTextView(
                modifier = Modifier.padding(top = dimensionResource(id = R.dimen.dimen_10dp)),
                title = stringResource(id = R.string.walletBalance),
                value = profileState.value?.walletBalance.toString(),
                valueStyle = MaterialTheme.typography.displaySmall.copy(
                    Color.Black
                )
            )

        }

    }
}


@Composable
private fun ProfileTextView(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    valueStyle: TextStyle
) {

    Column(modifier) {

        Text(
            text = title,
            modifier = Modifier,
            style = MaterialTheme.typography.titleSmall.copy(
                Color.Black
            )
        )
        Text(
            text = value,
            modifier = Modifier
                .padding(dimensionResource(id = R.dimen.dimen_10dp))
                .align(Alignment.Start),
            style = valueStyle
        )
    }
}


@Composable
fun ShowRewardProgramUI() {

    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.dimen_5dp))
                .clickable {
                    Toast
                        .makeText(
                            context,
                            context.getText(R.string.reward_toast),
                            Toast.LENGTH_LONG
                        )
                        .show()
                },
            text = stringResource(id = R.string.rewards),
            color = colorResource(id = R.color.purple_500),
            style = MaterialTheme.typography.titleMedium.copy(textAlign = TextAlign.Center)
        )
    }
}


