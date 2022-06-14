package com.rafsan.galleryapp.pages

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.rafsan.galleryapp.R
import com.rafsan.galleryapp.activity.MainActivity.Companion.MAIN_COLOR


@Composable
fun splashPage (navController: NavController) {

    val scale = remember {
        androidx.compose.animation.core.Animatable(0f)
    }

    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        scale.animateTo(
            targetValue = 0.7f,
            animationSpec = tween(
                durationMillis = 4000,
                easing = {
                    OvershootInterpolator(4f).getInterpolation(it)
                })
        )

        navController.popBackStack()
        navController.navigate("photo_list_page")
    }

    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.gallery_lottie))
        LottieAnimation(
            composition,
            iterations = LottieConstants.IterateForever,
            modifier = Modifier.size(250.dp)
        )

        Spacer(modifier = Modifier.height(50.dp))

        Text(text = "Gallery App",
            fontFamily = FontFamily(typeface = ResourcesCompat.getFont(context,
                R.font.nautilus)!!),
            fontSize = 35.sp, color = Color(MAIN_COLOR), textAlign = TextAlign.Center, modifier = Modifier
                .align(
                    Alignment.CenterHorizontally
                ))
    }
}



