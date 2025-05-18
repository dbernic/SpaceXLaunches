package com.dbernic.spacexlaunches.ui.screens.details

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withLink
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.dbernic.spacexlaunches.model.entities.ui.LaunchDetailsUi
import com.dbernic.spacexlaunches.ui.common.TopBar
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView


@Composable
fun DetailsScreen(
    launchId: String,
    viewModel: DetailsViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.getDetails(launchId)
    }

    DetailsView(
        details = viewModel.details.collectAsState().value,
        onFavoriteClick = { viewModel.switchFavorite(it) }
    )

}

@Composable
fun DetailsView(
    details: LaunchDetailsUi?,
    onFavoriteClick: (String)-> Unit
) {
    details?.let {
        Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp)) {
            TopBar(details.name, details.isFavorite, { onFavoriteClick(details.id) })

            details.youTubeURL?.let {
                YoutubeVideoPlayer(youtubeURL = it)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Description:", fontWeight = FontWeight.Bold)
            Text(details.description?:"Confidential")

            Spacer(modifier = Modifier.height(16.dp))

            Row (modifier = Modifier.fillMaxWidth()){
                Text("Rocket name: ", fontWeight = FontWeight.Bold)
                Text(details.rocketName)
            }
            Row (modifier = Modifier.fillMaxWidth()){
                Text("Payload weight: ", fontWeight = FontWeight.Bold)
                Text(details.payloadWeight)
            }
            details.wikiURL?.let {
                Text(
                    buildAnnotatedString {
                        withLink(
                            LinkAnnotation.Url(
                                it, TextLinkStyles(style = SpanStyle(color = Color.Blue))
                            )
                        ) { append("Wikipedia") }
                    }
                )
            }

        }
    }

}

@Composable
fun YoutubeVideoPlayer(
    modifier: Modifier = Modifier,
    youtubeURL: String?,
    isPlaying: (Boolean) -> Unit = {},
    isLoading: (Boolean) -> Unit = {},
    onVideoEnded: () -> Unit = {}
){
    val mContext = LocalContext.current
    val mLifeCycleOwner = LocalLifecycleOwner.current
    val videoId = splitLinkForVideoId(youtubeURL)
    var player : com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer ?= null
    val playerFragment = YouTubePlayerView(mContext)
    val playerStateListener = object : AbstractYouTubePlayerListener() {
        override fun onReady(youTubePlayer: com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer) {
            super.onReady(youTubePlayer)
            player = youTubePlayer
            youTubePlayer.cueVideo(videoId, 0f)
        }

        override fun onStateChange(
            youTubePlayer: com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer,
            state: PlayerConstants.PlayerState
        ) {
            super.onStateChange(youTubePlayer, state)
            when(state){
                PlayerConstants.PlayerState.BUFFERING -> {
                    isLoading.invoke(true)
                    isPlaying.invoke(false)
                }
                PlayerConstants.PlayerState.PLAYING -> {
                    isLoading.invoke(false)
                    isPlaying.invoke(true)
                }
                PlayerConstants.PlayerState.ENDED -> {
                    isPlaying.invoke(false)
                    isLoading.invoke(false)
                    onVideoEnded.invoke()
                }
                else -> {}
            }
        }

        override fun onError(
            youTubePlayer: com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer,
            error: PlayerConstants.PlayerError
        ) {
            super.onError(youTubePlayer, error)
            Log.e("YouTubePlayer","iFramePlayer Error Reason = $error")
        }
    }
    val playerBuilder = IFramePlayerOptions.Builder().apply {
        controls(1)
        fullscreen(0)
        autoplay(0)
        rel(1)
    }
    AndroidView(
        modifier = modifier.background(Color.DarkGray),
        factory = {
            playerFragment.apply {
                enableAutomaticInitialization = false
                initialize(playerStateListener, playerBuilder.build())
            }
        }
    )
    DisposableEffect(key1 = Unit, effect = {
        onDispose {
            playerFragment.removeYouTubePlayerListener(playerStateListener)
            playerFragment.release()
            player = null
        }
    })
    DisposableEffect(mLifeCycleOwner) {
        val lifecycle = mLifeCycleOwner.lifecycle
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> {
                    player?.pause()
                }
                Lifecycle.Event.ON_PAUSE -> {
                    player?.pause()
                }
                else -> {
                    //
                }
            }
        }
        lifecycle.addObserver(observer)
        onDispose {
            lifecycle.removeObserver(observer)
        }
    }
}

private fun splitLinkForVideoId(
    url: String?
): String {
    return (url!!.split("="))[1]
}

