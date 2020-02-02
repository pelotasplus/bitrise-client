package pl.pelotasplus.bitrise.domain.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import pl.pelotasplus.bitrise.data.api.models.BuildStatus

@Parcelize
data class Build(
    val slug: String,
    val status: BuildStatus,
    val commitMessage: String,
    val branch: String,
    val buildNumber: Int
) : Parcelable