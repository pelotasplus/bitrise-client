package pl.pelotasplus.bitrise.navigation

import androidx.navigation.NavDirections
import pl.pelotasplus.bitrise.domain.models.Project
import pl.pelotasplus.bitrise.features.projects_list.ProjectsListFragmentDirections

sealed class NavigationEvent {

    abstract fun toAction(): NavDirections

    data class ProjectDetails(
        val project: Project
    ) : NavigationEvent() {
        override fun toAction() =
            ProjectsListFragmentDirections.actionProjectListToProjectDetails(project)
    }
}