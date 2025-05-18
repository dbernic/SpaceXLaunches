package com.dbernic.spacexlaunches.ui.common

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    name: String,
    isFavoriteEnable: Boolean,
    onFavoritesClick: ()-> Unit
) {
    val icon = if (isFavoriteEnable) Icons.Default.Favorite else Icons.Default.FavoriteBorder

    TopAppBar(
        title = {
            Text(
                modifier = Modifier.padding(32.dp, 0.dp),
                text = name,
                fontSize = 18.sp
            )
        },

        actions = {
            IconButton(
                onClick = {
                    onFavoritesClick()
                }
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null
                )
            }
        }
    )
}