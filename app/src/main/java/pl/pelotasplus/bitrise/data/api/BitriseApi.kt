package pl.pelotasplus.bitrise.data.api

import io.reactivex.Single
import pl.pelotasplus.bitrise.data.api.models.AppsResponse
import pl.pelotasplus.bitrise.data.api.models.BuildsResponse
import pl.pelotasplus.bitrise.data.api.models.MeResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface BitriseApi {

    companion object {
        const val ENDPOINT = "https://api.bitrise.io/v0.1/"
    }

    @GET("me")
    suspend fun meCoro(@Header("Authorization") authorization: String): MeResponse

    @GET("apps")
    suspend fun appsCoro(@Header("Authorization") authorization: String): AppsResponse

    @GET("me")
    fun meRx(@Header("Authorization") authorization: String): Single<MeResponse>

    @GET("apps")
    fun appsRx(@Header("Authorization") authorization: String): Single<AppsResponse>

    @GET("apps/{appSlug}/builds")
    fun buildsRx(
        @Header("Authorization") authorization: String,
        @Path("appSlug") appSlug: String
    ): Single<BuildsResponse>

}