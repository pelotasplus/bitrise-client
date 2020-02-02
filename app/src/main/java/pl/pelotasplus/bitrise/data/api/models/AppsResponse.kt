package pl.pelotasplus.bitrise.data.api.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import pl.pelotasplus.bitrise.domain.models.Project
import pl.pelotasplus.bitrise.domain.models.ProjectType

@JsonClass(generateAdapter = true)
data class AppsResponse(
    val data: List<AppsDataResponse>
)

@JsonClass(generateAdapter = true)
data class AppsDataResponse(
    val title: String,
    val slug: String,
    @Json(name = "project_type") val projectType: String, // android
    val provider: String, // github
    @Json(name = "avatar_url") val avatarUrl: String?,
    @Json(name = "is_public") val public: Boolean,
    @Json(name = "is_disabled") val disabled: Boolean,
    val status: Int, // status
    @Json(name = "repo_owner") val repoOwner: String,
    @Json(name = "repo_url") val repoUrl: String,
    @Json(name = "repo_slug") val repoSlug: String
)

@JsonClass(generateAdapter = true)
data class AppsDataOwnerResponse(
    @Json(name = "account_type") val accountType: String,
    val name: String,
    val slug: String
)

fun AppsDataResponse.toProject() =
    Project(
        name = title,
        slug = slug,
        repoOwner = repoOwner,
        type = ProjectType.fromString(projectType)
    )