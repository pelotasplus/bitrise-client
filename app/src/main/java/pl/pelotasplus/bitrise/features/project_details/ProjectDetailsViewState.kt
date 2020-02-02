package pl.pelotasplus.bitrise.features.project_details

import androidx.lifecycle.MutableLiveData
import me.tatarka.bindingcollectionadapter2.ItemBinding
import pl.pelotasplus.bitrise.BR
import pl.pelotasplus.bitrise.R
import pl.pelotasplus.bitrise.domain.models.Project
import pl.pelotasplus.bitrise.features.projects_list.ProjectsListItemViewState
import pl.pelotasplus.bitrise.features.projects_list.ProjectsListViewModel

data class ProjectDetailsViewState(
    val snackBar: MutableLiveData<String> = MutableLiveData(),
    val isRefreshing: MutableLiveData<Boolean> = MutableLiveData(),
    val builds: MutableLiveData<List<BuildsListItemViewState>> = MutableLiveData(),
    val projectName: MutableLiveData<String> = MutableLiveData("")
) {
    fun getItemBinding(viewModel: ProjectDetailsViewModel) =
        ItemBinding.of<BuildsListItemViewState>(BR.viewState, R.layout.list_item_build)
            .bindExtra(BR.viewModel, viewModel)
}
