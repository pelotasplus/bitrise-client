package pl.pelotasplus.bitrise.features.project_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import pl.pelotasplus.bitrise.data.repository.BitriseRepository
import pl.pelotasplus.bitrise.domain.models.Project

class ProjectDetailsViewModel @AssistedInject constructor(
    @Assisted private val project: Project,
    bitriseRepository: BitriseRepository
) : ViewModel() {

    val viewState = ProjectDetailsViewState()

    private val retry = ConflatedBroadcastChannel(Unit)

    init {
        retry.asFlow()
            .onStart {
                viewState.projectName.value = project.name
                viewState.isRefreshing.value = true
            }
            .flowOn(Dispatchers.Main)
            .map {
                bitriseRepository.buildsCoro(project)
            }
            .map { builds ->
                builds.map { build -> build.toListItemViewState(project) }
            }
            .flowOn(Dispatchers.IO)
            .onEach { builds ->
                viewState.builds.value = builds
                viewState.isRefreshing.value = false
            }
            .catch { exc ->
                viewState.snackBar.value = exc.message
                viewState.isRefreshing.value = false
            }
            .launchIn(viewModelScope)
    }

    fun onBuildClicked(viewState: BuildsListItemViewState) {
        // navigate to build details/logs
    }

    fun onRefreshClicked() {
        retry.offer(Unit)
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(project: Project): ProjectDetailsViewModel
    }
}