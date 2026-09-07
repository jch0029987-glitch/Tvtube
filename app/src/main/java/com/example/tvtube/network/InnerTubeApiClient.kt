package com.example.tvtube.network

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

object InnerTubeApiClient {
    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
    }

    private const val BASE_URL = "https://www.youtube.com/youtubei/v1"

    suspend fun searchVideos(query: String): SearchResponse {
        val payload = mapOf(
            "context" to mapOf(
                "client" to mapOf(
                    "clientName" to "WEB",
                    "clientVersion" to "2.20240405.01.00",
                    "hl" to "en",
                    "gl" to "US"
                )
            ),
            "query" to query
        )

        return client.post("$BASE_URL/search") {
            contentType(ContentType.Application.Json)
            header(HttpHeaders.UserAgent, UserAgents.CHROME_DESKTOP)
            setBody(payload)
        }.body()
    }
}

@Serializable
data class SearchResponse(
    val contents: Contents? = null
)

@Serializable
data class Contents(
    val twoColumnSearchResultsRenderer: TwoColumnSearchResultsRenderer? = null
)

@Serializable
data class TwoColumnSearchResultsRenderer(
    val primaryContents: PrimaryContents? = null
)

@Serializable
data class PrimaryContents(
    val sectionListRenderer: SectionListRenderer? = null
)

@Serializable
data class SectionListRenderer(
    val contents: List<SectionContent>? = null
)

@Serializable
data class SectionContent(
    val itemSectionRenderer: ItemSectionRenderer? = null
)

@Serializable
data class ItemSectionRenderer(
    val contents: List<VideoItem>? = null
)

@Serializable
data class VideoItem(
    val videoRenderer: VideoRenderer? = null
)

@Serializable
data class VideoRenderer(
    val videoId: String,
    val title: RunWrapper,
    val thumbnail: ThumbnailWrapper
)

@Serializable
data class RunWrapper(
    val runs: List<Run>
)

@Serializable
data class Run(
    val text: String
)

@Serializable
data class ThumbnailWrapper(
    val thumbnails: List<Thumbnail>
)

@Serializable
data class Thumbnail(
    val url: String,
    val width: Int,
    val height: Int
)
