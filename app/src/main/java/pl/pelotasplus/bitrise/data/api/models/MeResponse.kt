package pl.pelotasplus.bitrise.data.api.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MeResponse(
    val data: MeDataResponse
)

@JsonClass(generateAdapter = true)
data class MeDataResponse(
    val username: String,
    val slug: String,
    val email: String,
    @Json(name = "avatar_url") val avatarUrl: String?
)