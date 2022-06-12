package com.rafsan.galleryapp.pages

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import androidx.navigation.NavController
import androidx.paging.ExperimentalPagingApi
import com.rafsan.galleryapp.utils.LoadingItem
import com.rafsan.galleryapp.view_model.MainViewModel
import com.skydoves.landscapist.glide.GlideImage
import java.io.File
import java.io.FileOutputStream


@ExperimentalPagingApi
@Composable
fun photoViewPage(
    navController: NavController,
    viewModel: MainViewModel,
) {

    Scaffold (bottomBar = { bottomView(viewModel) }) {
        GlideImage(imageModel = viewModel.imageURL, loading = {
            LoadingItem()
        }, success = { imageState ->
            imageState.drawable?.toBitmap().let {
                viewModel.imageData = it
                Image(bitmap = viewModel.imageData!!.asImageBitmap(), contentDescription = "Preview Image", contentScale = ContentScale.Crop)
            }

        })
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

            val outFile = File.createTempFile( "GalleryApps_${System.currentTimeMillis()}.png",".jpg",dir)
            try {
                outputStream = FileOutputStream(outFile)
                viewModel.imageData?.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                outputStream.flush()
                outputStream.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            val path: String = MediaStore.Images.Media.insertImage(context.contentResolver,
                viewModel.imageData,
                "Title",
                null)

            val shareIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_STREAM, Uri.fromFile(outFile))
                type = "image/jpeg"
            }
            context.startActivity(Intent.createChooser(shareIntent, null))
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








