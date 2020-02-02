package pl.pelotasplus.bitrise.features.project_details

import androidx.annotation.DrawableRes
import pl.pelotasplus.bitrise.R
import pl.pelotasplus.bitrise.data.api.models.BuildStatus
import pl.pelotasplus.bitrise.domain.models.Build
import pl.pelotasplus.bitrise.domain.models.Project

data class BuildsListItemViewState(
    val project: Project,
    val build: Build,
    @DrawableRes val statusIcon: Int
)

fun Build.toListItemViewState(project: Project) =
    BuildsListItemViewState(
        project = project,
        build = this,
        statusIcon = when (status) {
            BuildStatus.ON_HOLD -> R.drawable.ic_history_24
            BuildStatus.IN_PROGRESS -> R.drawable.ic_autorenew_24
            BuildStatus.SUCCESS -> R.drawable.ic_done_24
            BuildStatus.ERROR -> R.drawable.ic_error_outline_24
            BuildStatus.UNKNOWN -> R.drawable.ic_help_outline_24
        }
    )
