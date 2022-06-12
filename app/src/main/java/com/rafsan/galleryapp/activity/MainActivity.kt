package com.rafsan.galleryapp.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.paging.ExperimentalPagingApi
import com.rafsan.galleryapp.pages.photoListPage
import com.rafsan.galleryapp.pages.photoViewPage
import com.rafsan.galleryapp.ui.theme.GalleryAppTheme
import com.rafsan.galleryapp.view_model.MainViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalPagingApi::class)
    private val viewModel: MainViewModel by viewModels()



    @OptIn(ExperimentalPagingApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            runApplication(viewModel)
        }
    }


    @ExperimentalPagingApi
    @Composable
    private fun runApplication(viewModel: MainViewModel) {
        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = "photo_list_page", builder = {
            composable("photo_list_page", content = { photoListPage(navController, viewModel) })
            composable("photo_view_page", content = { photoViewPage(navController = navController,viewModel) })
        })
    }
}



@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    GalleryAppTheme {
        Greeting("Android")
    }
}