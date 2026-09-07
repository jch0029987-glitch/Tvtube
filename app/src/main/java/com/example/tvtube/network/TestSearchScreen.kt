package com.example.tvtube.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Button
import androidx.tv.material3.Surface
import androidx.tv.material3.Text
import com.example.tvtube.network.InnerTubeApiClient
import kotlinx.coroutines.launch

@Composable
fun TestSearchScreen() {
    val scope = rememberCoroutineScope()
    var rawJson by remember { mutableStateOf("Press Test to fetch search results...") }
    var titles = remember { mutableStateListOf<String>() }

    Surface(modifier = Modifier.fillMaxSize()) {
        Row(modifier = Modifier.fillMaxSize().padding(24.dp)) {
            // Left Column: Controls & Titles
            Column(modifier = Modifier.weight(1.dp).fillMaxHeight().padding(end = 16.dp)) {
                Button(onClick = {
                    scope.launch {
                        try {
                            val jsonResult = InnerTubeApiClient.searchRaw("Big Buck Bunny")
                            rawJson = jsonResult

                            val parsed = InnerTubeApiClient.searchVideos("Big Buck Bunny")
                            titles.clear()
                            
                            // Extract video titles safely across possible renderer structures
                            val items = parsed.contents?.twoColumnSearchResultsRenderer?.primaryContents
                                ?.sectionListRenderer?.contents
                                ?: parsed.contents?.sectionListRenderer?.contents

                            items?.forEach { section ->
                                section.itemSectionRenderer?.contents?.forEach { item ->
                                    item.videoRenderer?.title?.runs?.firstOrNull()?.text?.let { titles.add(it) }
                                    item.compactVideoRenderer?.title?.runs?.firstOrNull()?.text?.let { titles.add(it) }
                                }
                                section.musicShelfRenderer?.contents?.forEach { item ->
                                    item.videoRenderer?.title?.runs?.firstOrNull()?.text?.let { titles.add(it) }
                                }
                            }
                        } catch (e: Exception) {
                            rawJson = "Error: ${e.localizedMessage}"
                        }
                    }
                }) {
                    Text("Run InnerTube Search Test")
                }

                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Extracted Video Titles (${titles.size}):")

                LazyColumn(modifier = Modifier.fillMaxHeight().weight(1.dp)) {
                    items(titles) { title ->
                        Text(text = "- $title", modifier = Modifier.padding(vertical = 4.dp))
                    }
                }
            }

            // Right Column: Raw JSON Output
            Box(
                modifier = Modifier
                    .weight(1.dp)
                    .fillMaxHeight()
                    .background(Color.DarkGray)
                    .padding(12.dp)
            ) {
                LazyColumn {
                    item {
                        Text(text = rawJson, color = Color.White)
                    }
                }
            }
        }
    }
}
