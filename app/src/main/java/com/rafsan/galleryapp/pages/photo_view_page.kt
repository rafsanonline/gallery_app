package com.rafsan.galleryapp.pages

import android.content.Intent
import android.graphics.Bitmap
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.core.graphics.drawable.toBitmap
import androidx.navigation.NavController
import androidx.paging.ExperimentalPagingApi
import com.rafsan.galleryapp.R
import com.rafsan.galleryapp.activity.MainActivity.Companion.MAIN_COLOR
import com.rafsan.galleryapp.view_model.MainViewModel
import com.skydoves.landscapist.coil.CoilImage
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin


@ExperimentalPagingApi
@Composable
fun photoViewPage(
    navController: NavController,
    viewModel: MainViewModel,
) {

    Scaffold (Modifier.fillMaxSize(),bottomBar = { bottomView(viewModel) } , topBar = { topView(navController)}) {

        CoilImage(imageModel = viewModel.imageURL, success = { imageState ->
            imageState.drawable?.toBitmap().let {
                viewModel.imageData = it
                ImagePreview(link = it!!)
            }
        })
    }

}


@Composable
fun topView(navController: NavController) {

    Box(modifier = Modifier
        .size(70.dp)
        .padding(start = 20.dp, top = 20.dp)
        .clip(shape = RoundedCornerShape(100))
        .background(Color(MAIN_COLOR))) {
      Icon(painter = painterResource(R.drawable.back), contentDescription = "", tint = Color.White,
          modifier = Modifier.size(25.dp).align(
              Alignment.Center).clickable { navController.popBackStack() } )
    }

}


@ExperimentalPagingApi
@Composable
fun bottomView(viewModel: MainViewModel) {

    val context = LocalContext.current

    Row(Modifier
        .fillMaxWidth()
        .height(70.dp)
        .padding(start = 20.dp, end = 20.dp, bottom = 30.dp)
        .clip(RoundedCornerShape(20.dp))
        .background(Color(
            0x46FFFFFF)), verticalAlignment = Alignment.CenterVertically,horizontalArrangement = Arrangement.SpaceEvenly) {

        Icon(Icons.Filled.Share, "menu",Modifier.clickable {
            var outputStream: FileOutputStream? = null
            val dir = File("/storage/emulated/0/DCIM/GalleryApps/.temp/")
            if (!dir.exists()) {dir.mkdirs()}

            val outFile = File.createTempFile( "GalleryApps_${System.currentTimeMillis()}.jpg",".jpg",dir)
            try {
                outputStream = FileOutputStream(outFile)
                viewModel.imageData?.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                outputStream.flush()
                outputStream.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            try {
                val shareIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_STREAM,  FileProvider.getUriForFile(
                        context,
                        "com.rafsan.galleryapp.fileprovider",
                        outFile))
                    type = "image/jpeg"
                }

                context.startActivity(Intent.createChooser(shareIntent, null))
            }
            catch (e: IOException) {

            }



        })
        Icon(Icons.Filled.Send, "menu", Modifier.clickable {
            //context.startActivity(shareIntent)

            var outputStream: FileOutputStream? = null
            val dir = File("/storage/emulated/0/DCIM/GalleryApps")
            if (!dir.exists()) {dir.mkdirs()}

            val outFile = File(dir, "GalleryApps_${System.currentTimeMillis()}.jpg")
            try {
                outputStream = FileOutputStream(outFile)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            viewModel.imageData?.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            Toast.makeText(context, "Photo Saved", Toast.LENGTH_SHORT).show()
            try {
                outputStream?.flush()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            try {
                outputStream?.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }

        })
    }
}

@Composable
fun ImagePreview(link: Bitmap) {
    Box(modifier = Modifier.fillMaxSize()) {
        var angle by remember { mutableStateOf(0f) }
        var zoom by remember { mutableStateOf(1f) }
        var offsetX by remember { mutableStateOf(0f) }
        var offsetY by remember { mutableStateOf(0f) }

        CoilImage(
            imageModel = link,
            contentDescription = "image",
            modifier = Modifier
                .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
                .graphicsLayer(
                    scaleX = zoom,
                    scaleY = zoom,
                    rotationZ = angle
                )
                .pointerInput(Unit) {
                    detectTransformGestures(
                        onGesture = { _, pan, gestureZoom, gestureRotate ->
                            // angle += gestureRotate
                            zoom *= gestureZoom
                            val x = pan.x * zoom
                            val y = pan.y * zoom
                            val angleRad = angle * PI / 180.0
                            offsetX += (x * cos(angleRad) - y * sin(angleRad)).toFloat()
                            offsetY += (x * sin(angleRad) + y * cos(angleRad)).toFloat()
                        }
                    )
                }
                .fillMaxSize()
        )
    }
}





