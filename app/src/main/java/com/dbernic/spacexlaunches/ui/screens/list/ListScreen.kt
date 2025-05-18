package com.dbernic.spacexlaunches.ui.screens.list

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import com.dbernic.spacexlaunches.R
import com.dbernic.spacexlaunches.model.entities.ui.LaunchItemUI
import com.dbernic.spacexlaunches.ui.common.TopBar


@Composable
fun ListScreen(
    navigateDetails: (String) -> Unit,
    viewModel: ListViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.fetchItems()
    }

    ListView(
        launchItems = viewModel.showItemsFlow.collectAsState(),
        isFavoriteEnable = viewModel.isShowFavoritesFlow.collectAsState(),
        onItemClick = navigateDetails,
        onFavoriteClick = viewModel::switchFavorite,
        onFavoritesClick = viewModel::toggleFavorites
    )
}

@Composable
fun ListView(
    launchItems: State<List<LaunchItemUI>>,
    isFavoriteEnable: State<Boolean>,
    onItemClick: (String) -> Unit,
    onFavoriteClick: (String) -> Unit,
    onFavoritesClick: () -> Unit
) {
    Column {
        TopBar(stringResource(id = R.string.app_name), isFavoriteEnable.value, onFavoritesClick)

        LazyColumn {
            items(launchItems.value) { item ->
                ListItemView(
                    item = item,
                    onClick = { onItemClick(item.id) },
                    onFavoriteClick = { onFavoriteClick(item.id) }
                )
            }
        }
    }
}



@Composable
fun ListItemView(
    item: LaunchItemUI,
    onClick: () -> Unit,
    onFavoriteClick: () -> Unit
) {
    Box(modifier = Modifier.padding(bottom = 16.dp, start = 16.dp, end = 16.dp)) {
        Card(
            modifier = Modifier.fillMaxWidth().clickable { onClick() }
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                Box(
                    modifier = Modifier.height(250.dp).fillMaxWidth(),
                ) {
                    if (item.imageURL == null) {
                        Image(
                            painterResource(R.drawable.noimage),
                            contentDescription = null
                        )
                    } else {
                        AsyncImage(
                            ImageRequest.Builder(LocalContext.current)
                                .data(item.imageURL)
                                .build(),
                            placeholder = painterResource(R.drawable.placeholder),
                            contentDescription = null,
                            contentScale = ContentScale.Crop
                        )
                    }

                    val icon =
                        if (item.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder
                    Icon(
                        imageVector = icon,
                        contentDescription = "Icon",
                        modifier = Modifier.clickable { onFavoriteClick() }
                            .align(Alignment.TopEnd)
                            .padding(8.dp),
                        tint = Color.Red
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(item.name)
                    Spacer(modifier = Modifier.weight(1f))
                    Text(item.launchDate)
                }
            }


        }
    }
}
