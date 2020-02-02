package pl.pelotasplus.bitrise.features.projects_list

import androidx.annotation.DrawableRes
import pl.pelotasplus.bitrise.domain.models.Project

data class ProjectsListItemViewState(
    val project: Project,
    val name: String,
    val repoOwner: String,
    @DrawableRes val platformIcon: Int
)

fun Project.toListItemViewState() =
    ProjectsListItemViewState(
        project = this,
        name = name,
        platformIcon = type.icon,
        repoOwner = repoOwner
    )
