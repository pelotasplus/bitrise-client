package pl.pelotasplus.bitrise.di

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import pl.pelotasplus.bitrise.data.api.BitriseApi
import pl.pelotasplus.bitrise.di.AppModule.Companion.DEV_BUILD
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Named

@Module
class NetworkModule {

    companion object {
        const val API_ENDPOINT = "API_ENDPOINT"
        const val API_TOKEN = "API_TOKEN"
    }

    @Provides
    fun loggingInterceptor(@Named(DEV_BUILD) devBuild: Boolean) =
        HttpLoggingInterceptor().apply {
            level = if (devBuild)
                HttpLoggingInterceptor.Level.BODY
            else
                HttpLoggingInterceptor.Level.NONE
        }

    @Provides
    fun client(loggingInterceptor: HttpLoggingInterceptor) =
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

    @Provides
    fun retrofit(
        @Named(API_ENDPOINT) endpoint: String,
        client: OkHttpClient
    ): BitriseApi =
        Retrofit.Builder()
            .baseUrl(endpoint)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(BitriseApi::class.java)

}