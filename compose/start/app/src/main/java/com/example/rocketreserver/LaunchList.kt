@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.rocketreserver

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Optional

@Composable
fun LaunchList(onLaunchClick: (launchId: String) -> Unit) {
    var launchList by remember { mutableStateOf(emptyList<LaunchListQuery.Launch>()) }
    LaunchedEffect(Unit) {
        val response = apolloClient.query(LaunchListQuery()).execute()
        launchList = response.data?.launches?.launches?.filterNotNull() ?: emptyList()
        // Note: the .filterNotNull() is necessary because the schema defines launches as a list of nullable Launch objects.
        //Log.d("LaunchList", "Success ${response.data}")
    }

    LazyColumn {
        items(launchList) { launch ->
            LaunchItem(launch = launch, onClick = onLaunchClick)
        }
    }
}

@Composable
private fun LaunchItem(launch: LaunchListQuery.Launch, onClick: (launchId: String) -> Unit) {
    ListItem(
        modifier = Modifier.clickable { onClick(launch.id) },
        headlineContent = {
            // Mission name
            Text(text = launch.mission?.name ?: "")
        },
        supportingContent = {
            // Site
            Text(text = launch.site ?: "")
        },
        leadingContent = {
            // Mission patch
            AsyncImage(
                modifier = Modifier.size(68.dp, 68.dp),
                model = launch.mission?.missionPatch,
                placeholder = painterResource(R.drawable.ic_placeholder),
                error = painterResource(R.drawable.ic_placeholder),
                contentDescription = "Mission patch"
            )
        }
    )
}

@Composable
private fun LoadingItem() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        CircularProgressIndicator()
    }
}
