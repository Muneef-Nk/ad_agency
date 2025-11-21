package com.example.ad_display.ui

import android.app.Activity
import android.content.pm.ActivityInfo
import android.net.Uri
import androidx.annotation.OptIn
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.ad_display.model.AdResponse
import com.example.ad_display.network.ApiClient
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.delay
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.SimpleExoPlayer
import androidx.media3.common.Player
import androidx.media3.ui.PlayerView
import androidx.navigation.NavController
import retrofit2.Response


@OptIn(UnstableApi::class)
@Composable
fun AdsScreen(navController: NavController) {
    val context = LocalContext.current
    val activity = context as? Activity

    // Force landscape
    DisposableEffect(Unit) {
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        onDispose { activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED }
    }

    var ad by remember { mutableStateOf<AdResponse?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Fetch ad every 10 seconds
    LaunchedEffect(Unit) {
        while (true) {
            try {
                isLoading = true
                val tokenValue = TokenManager.getAccessToken(context).first()
                val token = "Bearer $tokenValue"

                val response: Response<AdResponse> = ApiClient.apiService.getAds(token)

                when (response.code()) {

                    200 -> {
                        val body = response.body()
                        ad = body
                        errorMessage = null
                    }

                    404 -> {
                        errorMessage = "No ad assigned yet. Contact admin."
                        ad = null
                    }

                    else -> {
                        errorMessage = "Server error: ${response.code()} - ${response.message()}"
                        ad = null
                    }
                }

            } catch (e: Exception) {
                errorMessage = "Network error: ${e.message}"
                ad = null
            } finally {
                isLoading = false
            }

            delay(10_000)
        }
    }


    // UI
    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(
                    onDoubleTap = {
                        navController.navigate("login") {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            },
        contentAlignment = Alignment.Center
    ) {

        when {
            isLoading -> CircularProgressIndicator()

            errorMessage != null -> {
                val isNoAd = errorMessage?.contains("No ad assigned yet") == true

                Text(
                    text = errorMessage ?: "Error",
                    fontSize = if (isNoAd) 36.sp else 18.sp,
                    fontWeight = if (isNoAd) FontWeight.Bold else FontWeight.Normal,
                    modifier = Modifier.padding(16.dp)
                )
            }

            ad != null -> {
                val url = ad!!.videoUrl

                if (url.endsWith(".mp4")) {
                    val player = remember(context) {
                        SimpleExoPlayer.Builder(context).build().apply {
                            setMediaItem(MediaItem.fromUri(Uri.parse(url)))
                            repeatMode = Player.REPEAT_MODE_ONE
                            playWhenReady = true
                            prepare()
                        }
                    }

                    DisposableEffect(player) {
                        onDispose { player.release() }
                    }

                    AndroidView(factory = { ctx ->
                        PlayerView(ctx).apply {
                            this.player = player
                            this.useController = false
                        }
                    }, modifier = Modifier.fillMaxSize())

                } else {
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(url)
                            .crossfade(true)
                            .build(),
                        contentDescription = "Ad",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
    }
}
