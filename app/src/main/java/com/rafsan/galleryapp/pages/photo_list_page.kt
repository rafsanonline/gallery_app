package com.rafsan.galleryapp.pages

import android.Manifest
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.rafsan.galleryapp.utils.LoadingItem
import com.rafsan.galleryapp.utils.LoadingView
import com.rafsan.galleryapp.view_model.MainViewModel
import com.skydoves.landscapist.ShimmerParams
import com.skydoves.landscapist.glide.GlideImage


@OptIn(ExperimentalPermissionsApi::class)
@ExperimentalPagingApi
@Composable
fun photoListPage(navController: NavController, viewModel: MainViewModel) {

    val multiplePermissionsState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
    ) { permissionStateMap ->
        viewModel.isPermissionGranted = !permissionStateMap.containsValue(false)
        if (viewModel.isPermissionGranted) {
            navController.navigate("photo_view_page")
        }
    }

    val data = viewModel.data?.collectAsLazyPagingItems()
    Scaffold {
        LazyVerticalGrid(cells = GridCells.Fixed(3), content = {
            if (data != null) {
                items(data.itemCount) { item ->
                    GlideImage(imageModel = data[item]?.urls?.small, modifier = Modifier
                        .size(150.dp)
                        .padding(1.dp)
                        .clickable {
                            viewModel.imageURL = data[item]?.urls?.regular
                            if (viewModel.isPermissionGranted) {
                                navController.navigate("photo_view_page")
                            } else {
                                multiplePermissionsState.launchMultiplePermissionRequest()
                            }
                        }, shimmerParams = ShimmerParams(
                        baseColor = Color(0xFFF5E419),
                        highlightColor = Color(0xFFF7F7F7),
                        durationMillis = 350,
                        dropOff = 0.65f,
                        tilt = 20f
                    ))
                }
            }
        })
    }
    data?.apply {
        when {
            loadState.refresh is LoadState.Loading -> {
                LoadingView(modifier = Modifier.fillMaxSize())
            }
            loadState.append is LoadState.Loading -> {
                LoadingItem()
            }
            loadState.refresh is LoadState.Error -> {

            }
            loadState.append is LoadState.Error -> {

            }
        }
    }

}