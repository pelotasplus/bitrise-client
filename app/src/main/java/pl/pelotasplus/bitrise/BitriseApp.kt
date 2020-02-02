package pl.pelotasplus.bitrise

import android.app.Application
import pl.pelotasplus.bitrise.data.api.BitriseApi
import pl.pelotasplus.bitrise.di.AppComponent
import pl.pelotasplus.bitrise.di.DaggerAppComponent

class BitriseApp : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.builder()
            .application(this)
            .apiEndpoint(BitriseApi.ENDPOINT)
            .apiToken(BuildConfig.BITRISE_TOKEN)
            .devBuild(BuildConfig.DEBUG)
            .build()
    }
}