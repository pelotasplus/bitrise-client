package pl.pelotasplus.bitrise.features.project_details

import androidx.lifecycle.ViewModel
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.subjects.PublishSubject
import pl.pelotasplus.bitrise.data.repository.BitriseRepository
import pl.pelotasplus.bitrise.di.AppModule
import pl.pelotasplus.bitrise.domain.models.Project
import javax.inject.Named

class ProjectDetailsViewModel @AssistedInject constructor(
    @Assisted private val project: Project,
    bitriseRepository: BitriseRepository,
    @Named(AppModule.IO_SCHEDULER) ioScheduler: Scheduler,
    @Named(AppModule.UI_SCHEDULER) uiScheduler: Scheduler
) : ViewModel() {

    val viewState = ProjectDetailsViewState()

    private val compositeDisposable = CompositeDisposable()
    private val retry = PublishSubject.create<Unit>()

    init {
        retry.startWith(Unit)
            .flatMapSingle {
                bitriseRepository.buildsRx(project)
                    .subscribeOn(ioScheduler)
            }
            .map { builds ->
                builds.map { build -> build.toListItemViewState(project) }
            }
            .observeOn(uiScheduler)
            .doOnSubscribe {
                viewState.projectName.value = project.name
                viewState.isRefreshing.value = true
            }
            .subscribeBy(
                onNext = { builds ->
                    viewState.builds.value = builds
                    viewState.isRefreshing.value = false
                },
                onError = {
                    viewState.isRefreshing.value = false
                }
            )
            .addTo(compositeDisposable)
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }

    fun onBuildClicked(viewState: BuildsListItemViewState) {
        // navigate to build details/logs
    }

    fun onRefreshClicked() {
        retry.onNext(Unit)
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(project: Project): ProjectDetailsViewModel
    }
}