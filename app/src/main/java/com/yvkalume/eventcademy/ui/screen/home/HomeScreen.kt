package com.yvkalume.eventcademy.ui.screen.home

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.SubcomposeAsyncImage
import com.yvkalume.eventcademy.R
import com.yvkalume.eventcademy.data.entity.Advertisement
import com.yvkalume.eventcademy.data.entity.AdvertisementType
import com.yvkalume.eventcademy.data.entity.Event
import com.yvkalume.eventcademy.data.entity.User
import com.yvkalume.eventcademy.data.entity.fakeAdvertisementList
import com.yvkalume.eventcademy.ui.MainActivity
import com.yvkalume.eventcademy.ui.components.AdvertisementsItem
import com.yvkalume.eventcademy.ui.components.EventItem
import com.yvkalume.eventcademy.ui.components.SectionHeader
import com.yvkalume.eventcademy.util.ThemePreview

@Composable
fun HomeRoute(viewModel: HomeViewModel = hiltViewModel(), onEventClick: (String) -> Unit) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(uiState) {
        Log.d("HomeRoute", "uiState: $uiState")
    }
    BackHandler {
        (context as? MainActivity)?.finish()
    }
    HomeScreen(uiState = uiState, currentUser = currentUser, onEventClick = onEventClick)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreen(uiState: HomeUiState, currentUser: User?, onEventClick: (String) -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.padding(start = 16.dp, end = 4.dp),
                navigationIcon = {
                    SubcomposeAsyncImage(
                        model = currentUser?.photoUrl ?: R.drawable.eventcademy_app_icon,
                        loading = {
                            Icon(
                                painter = painterResource(id = R.drawable.eventcademy_app_icon),
                                contentDescription = null
                            )
                        },
                        contentDescription = null,
                        modifier = Modifier
                            .size(46.dp)
                            .clip(RoundedCornerShape(16.dp))
                    )
                },
                title = {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Text(text = "Salut !")
                        Text(
                            text = currentUser?.name ?: "",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(imageVector = Icons.Outlined.Settings, contentDescription = null)
                    }
                }
            )
        }
    ) {
        Box(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            when (uiState) {
                HomeUiState.Empty -> {}
                is HomeUiState.Error -> {}
                HomeUiState.Loading -> {
                    CircularProgressIndicator()
                }

                is HomeUiState.Success -> {
                    HomeScreenContent(
                        modifier = Modifier.fillMaxSize(),
                        ads = uiState.advertisements,
                        events = uiState.events,
                        onEventClick = onEventClick
                    )
                }
            }
        }
    }
}

@Composable
private fun HomeScreenContent(
    ads: List<Advertisement>,
    events: List<Event>,
    onEventClick: (eventUid: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val uriHandler = LocalUriHandler.current

    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (ads.isNotEmpty()) {
            item(span = { GridItemSpan(2) }) {
                SectionHeader(title = "Annonces")
            }
            item(span = { GridItemSpan(2) }, key = "ads") {
                AdvertisementsItem(
                    items = fakeAdvertisementList,
                    onItemClick = { advertisement ->
                        if (advertisement.type == AdvertisementType.INTERNAL_EVENT) {
                            onEventClick(advertisement.destination)
                        } else if (advertisement.type == AdvertisementType.EXTERNAL_CONTENT) {
                            uriHandler.openUri(advertisement.destination)
                        }
                    },
                    modifier = Modifier.aspectRatio(1.2f)
                )
            }
        }
        if (events.isNotEmpty()) {
            item(span = { GridItemSpan(2) }) {
                SectionHeader(title = "Événements à venir")
            }
            items(items = events, key = { it.uid }) { event ->
                EventItem(event = event, onClick = { onEventClick(event.uid) })
            }
        }
    }
}


@Preview
@Composable
fun HomeScreenPreview() {
    ThemePreview {
        HomeScreen(uiState = HomeUiState.Loading, currentUser = null, onEventClick = {})
    }
}