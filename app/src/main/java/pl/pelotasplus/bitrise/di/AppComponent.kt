package pl.pelotasplus.bitrise.di

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import pl.pelotasplus.bitrise.di.AppModule.Companion.DEV_BUILD
import pl.pelotasplus.bitrise.di.NetworkModule.Companion.API_ENDPOINT
import pl.pelotasplus.bitrise.di.NetworkModule.Companion.API_TOKEN
import pl.pelotasplus.bitrise.features.project_details.ProjectDetailsViewModel
import pl.pelotasplus.bitrise.features.projects_list.ProjectsListViewModel
import javax.inject.Named
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, NetworkModule::class, AssistedInjectModule::class])
interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        @BindsInstance
        fun apiEndpoint(@Named(API_ENDPOINT) endpoint: String): Builder

        @BindsInstance
        fun apiToken(@Named(API_TOKEN) token: String): Builder

        @BindsInstance
        fun devBuild(@Named(DEV_BUILD) devBuild: Boolean): Builder

        fun build(): AppComponent
    }

    val projectsListViewModel: ProjectsListViewModel

    val projectDetailsViewModelFactory: ProjectDetailsViewModel.Factory

}