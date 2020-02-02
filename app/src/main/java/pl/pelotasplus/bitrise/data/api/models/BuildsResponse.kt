package pl.pelotasplus.bitrise.data.api.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import pl.pelotasplus.bitrise.domain.models.Build

@JsonClass(generateAdapter = true)
class BuildsResponse(
    val data: List<BuildsDataResponse>
)

@JsonClass(generateAdapter = true)
data class BuildsDataResponse(
    @Json(name = "status") val status: Int,
    @Json(name = "status_text") val statusText: String,
    @Json(name = "commit_message") val commitMessage: String?,
    val slug: String,
    val branch: String,
    @Json(name = "build_number") val buildNumber: Int
)

fun BuildsDataResponse.toBuild() =
    Build(
        slug = slug,
        status = BuildStatus.fromStatus(status, statusText),
        commitMessage = commitMessage ?: "(no commit message)",
        branch = branch,
        buildNumber = buildNumber
    )

enum class BuildStatus {
    ON_HOLD,
    IN_PROGRESS,
    SUCCESS,
    ERROR,
    UNKNOWN;

    companion object {
        fun fromStatus(status: Int, statusText: String) =
            when (status) {
                0 -> when (statusText) {
                    "in-progress" -> IN_PROGRESS
                    else -> ON_HOLD
                }
                1 -> SUCCESS
                2 -> ERROR
                else -> UNKNOWN
            }
    }
}