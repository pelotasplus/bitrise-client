package pl.pelotasplus.bitrise.domain.models

import android.os.Parcelable
import androidx.annotation.DrawableRes
import kotlinx.android.parcel.Parcelize
import pl.pelotasplus.bitrise.R

@Parcelize
data class Project(
    val name: String,
    val slug: String,
    val repoOwner: String,
    val type: ProjectType
) : Parcelable

enum class ProjectType(
    @DrawableRes val icon: Int
) {
    ANDROID(R.drawable.ic_android_24),
    IOS(R.drawable.ic_attach_money_24),
    UNKNOWN(R.drawable.ic_help_outline_24);

    companion object {
        fun fromString(value: String) =
            when (value) {
                "android" -> ANDROID
                "ios" -> IOS
                else -> UNKNOWN
            }
    }
}