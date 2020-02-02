package pl.pelotasplus.bitrise.features.projects_list

import androidx.annotation.IdRes
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import pl.pelotasplus.bitrise.R
import pl.pelotasplus.bitrise.data.repository.BitriseRepository
import pl.pelotasplus.bitrise.di.AppModule.Companion.IO_SCHEDULER
import pl.pelotasplus.bitrise.di.AppModule.Companion.UI_SCHEDULER
import pl.pelotasplus.bitrise.navigation.NavigationEvent
import java.util.Locale
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Named

class ProjectsListViewModel @Inject constructor(
    private val bitriseRepository: BitriseRepository,
    @Named(IO_SCHEDULER) private val ioScheduler: Scheduler,
    @Named(UI_SCHEDULER) private val uiScheduler: Scheduler
) : ViewModel() {

    private val _navigationChannel = PublishSubject.create<NavigationEvent>()
    val navigation: Observable<NavigationEvent> = _navigationChannel
    val viewState = ProjectsListViewState()

    private val compositeDisposable = CompositeDisposable()
    private val retry = PublishSubject.create<Unit>()
    private val sortBy = BehaviorSubject.createDefault(SortMethod.NONE)
    private val filter = BehaviorSubject.createDefault<String>("")

    private val nameFilterObserver = Observer<String> {
        filter.onNext(it)
    }

    init {
        viewState.nameFilter.observeForever(nameFilterObserver)

        retry.startWith(Unit)
            .flatMapSingle {
                bitriseRepository.appsRx()
                    .subscribeOn(ioScheduler)
            }
            .map { projects ->
                projects.map { project -> project.toListItemViewState() }
            }
            .flatMap { projects ->
                sortBy.map { sortMethod ->
                    projects.sortedBy {
                        when (sortMethod) {
                            SortMethod.NONE -> null
                            SortMethod.BY_PROJECT_NAME -> it.name.toLowerCase(Locale.getDefault())
                            SortMethod.BY_REPO_OWNER -> it.repoOwner.toLowerCase(Locale.getDefault())
                        }
                    }
                }
            }
            .switchMap { projects ->
                filter
                    .debounce(500L, TimeUnit.MILLISECONDS, ioScheduler)
                    .distinctUntilChanged()
                    .map { filterValue ->
                        projects.filter { it.name.contains(filterValue, ignoreCase = true) }
                    }
            }
            .observeOn(uiScheduler)
            .doOnSubscribe {
                viewState.isRefreshing.value = true
            }
            .subscribeBy(
                onNext = {
                    viewState.projects.value = it
                    viewState.isRefreshing.value = false
                },
                onError = { exc ->
                    viewState.isRefreshing.value = false
                    viewState.snackBar.value = exc.message
                }
            )
            .addTo(compositeDisposable)
    }

    fun onRefreshClicked() {
        retry.onNext(Unit)
    }

    fun onProjectClicked(viewState: ProjectsListItemViewState) {
        _navigationChannel.onNext(NavigationEvent.ProjectDetails(viewState.project))
    }

    fun onSortMethodChanged(@IdRes itemId: Int) {
        sortBy.onNext(SortMethod.fromMenuItemId(itemId))
    }

    override fun onCleared() {
        viewState.nameFilter.removeObserver(nameFilterObserver)
        compositeDisposable.clear()
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