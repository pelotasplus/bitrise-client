package pl.pelotasplus.bitrise.di

import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Named

@Module
class AppModule {

    companion object {
        const val DEV_BUILD = "DEV_BUILD"
        const val IO_SCHEDULER = "IO_SCHEDULER"
        const val UI_SCHEDULER = "UI_SCHEDULER"
    }

    @Provides
    fun providesMoshi(): Moshi = Moshi.Builder().build()

    @Provides
    @Named(IO_SCHEDULER)
    fun provideIoScheduler() = Schedulers.io()

    @Provides
    @Named(UI_SCHEDULER)
    fun provideUiScheduler() = AndroidSchedulers.mainThread()
}