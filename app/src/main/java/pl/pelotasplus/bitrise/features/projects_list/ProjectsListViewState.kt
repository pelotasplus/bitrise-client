package pl.pelotasplus.bitrise.features.projects_list

import androidx.lifecycle.MutableLiveData
import me.tatarka.bindingcollectionadapter2.ItemBinding
import pl.pelotasplus.bitrise.BR
import pl.pelotasplus.bitrise.R

data class ProjectsListViewState(
    val snackBar: MutableLiveData<String> = MutableLiveData(),
    val isRefreshing: MutableLiveData<Boolean> = MutableLiveData(),
    val filterMode: MutableLiveData<Boolean> = MutableLiveData(false),
    val nameFilter: MutableLiveData<String> = MutableLiveData(),
    val projects: MutableLiveData<List<ProjectsListItemViewState>> = MutableLiveData()
) {
    fun getItemBinding(viewModel: ProjectsListViewModel) =
        ItemBinding.of<ProjectsListItemViewState>(BR.viewState, R.layout.list_item_project)
            .bindExtra(BR.viewModel, viewModel)
}
