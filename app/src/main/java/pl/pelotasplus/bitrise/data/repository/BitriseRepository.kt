package pl.pelotasplus.bitrise.data.repository

import pl.pelotasplus.bitrise.data.api.BitriseApi
import pl.pelotasplus.bitrise.data.api.models.toBuild
import pl.pelotasplus.bitrise.data.api.models.toProject
import pl.pelotasplus.bitrise.di.NetworkModule.Companion.API_TOKEN
import pl.pelotasplus.bitrise.domain.models.Project
import javax.inject.Inject
import javax.inject.Named

class BitriseRepository @Inject constructor(
    private val bitriseApi: BitriseApi,
    @Named(API_TOKEN) private val authorizationToken: String
) {

    suspend fun me() =
        bitriseApi.meCoro(authorizationToken)

    suspend fun appsCoro() =
        bitriseApi.appsCoro(authorizationToken)
            .data
            .map { it.toProject() }

    fun appsRx() =
        bitriseApi.appsRx(authorizationToken)
            .map { it.data }
            .map { it.map { appData -> appData.toProject() } }

    fun buildsRx(project: Project) =
        bitriseApi.buildsRx(authorizationToken, project.slug)
            .map { it.data }
            .map { it.map { buildData -> buildData.toBuild() } }

}