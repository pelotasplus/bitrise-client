package pl.pelotasplus.bitrise.features.projects_list

import androidx.annotation.IdRes
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import pl.pelotasplus.bitrise.R
import pl.pelotasplus.bitrise.data.repository.BitriseRepository
import pl.pelotasplus.bitrise.navigation.NavigationEvent
import java.util.Locale
import javax.inject.Inject

class ProjectsListViewModel @Inject constructor(
    private val bitriseRepository: BitriseRepository
) : ViewModel() {

    private val _navigationChannel = PublishSubject.create<NavigationEvent>()
    val navigation: Observable<NavigationEvent> = _navigationChannel
    val viewState = ProjectsListViewState()

    private val retry = ConflatedBroadcastChannel(Unit)
    private val sortBy = ConflatedBroadcastChannel(SortMethod.NONE)
    private val filter = ConflatedBroadcastChannel("")

    private val nameFilterObserver = Observer<String> {
        filter.offer(it)
    }

    init {
        viewState.nameFilter.observeForever(nameFilterObserver)

        retry.asFlow()
            .onStart {
                viewState.isRefreshing.value = true
            }
            .flowOn(Dispatchers.Main)
            .map {
                bitriseRepository.appsCoro()
            }
            .map { projects ->
                projects.map { project -> project.toListItemViewState() }
            }
            .combine(sortBy.asFlow()) { projects, sortMethod ->
                projects.sortedBy {
                    when (sortMethod) {
                        SortMethod.NONE -> null
                        SortMethod.BY_PROJECT_NAME -> it.name.toLowerCase(Locale.getDefault())
                        SortMethod.BY_REPO_OWNER -> it.repoOwner.toLowerCase(Locale.getDefault())
                    }
                }
            }
            .combine(
                filter.asFlow()
                    .debounce(500L)
                    .distinctUntilChanged()
            ) { projects, filterValue ->
                projects.filter { it.name.contains(filterValue, ignoreCase = true) }
            }
            .flowOn(Dispatchers.IO)
            .onEach {
                viewState.projects.value = it
                viewState.isRefreshing.value = false
            }
            .catch { exc ->
                viewState.snackBar.value = exc.message
            }
            .launchIn(viewModelScope)
    }

    fun onRefreshClicked() {
        retry.offer(Unit)
    }

    fun onProjectClicked(viewState: ProjectsListItemViewState) {
        _navigationChannel.onNext(NavigationEvent.ProjectDetails(viewState.project))
    }

    fun onSortMethodChanged(@IdRes itemId: Int) {
        sortBy.offer(SortMethod.fromMenuItemId(itemId))
    }

    override fun onCleared() {
        viewState.nameFilter.removeObserver(nameFilterObserver)
        super.onCleared()
    }

    fun onToggleFilterMode() {
        if (viewState.filterMode.value == true) {
            viewState.filterMode.value = false
            viewState.nameFilter.value = ""
        } else {
            viewState.filterMode.value = true
        }
    }
}

enum class SortMethod {
    NONE,
    BY_PROJECT_NAME,
    BY_REPO_OWNER;

    companion object {
        fun fromMenuItemId(@IdRes menuItemId: Int) =
            when (menuItemId) {
                R.id.sort_by_owner -> BY_REPO_OWNER
                R.id.sort_by_project_name -> BY_PROJECT_NAME
                else -> NONE
            }
    }
}