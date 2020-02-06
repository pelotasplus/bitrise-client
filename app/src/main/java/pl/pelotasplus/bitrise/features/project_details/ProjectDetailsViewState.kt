package pl.pelotasplus.bitrise.features.project_details

import androidx.lifecycle.MutableLiveData
import me.tatarka.bindingcollectionadapter2.ItemBinding
import pl.pelotasplus.bitrise.BR
import pl.pelotasplus.bitrise.R

data class ProjectDetailsViewState(
    val isRefreshing: MutableLiveData<Boolean> = MutableLiveData(),
    val builds: MutableLiveData<List<BuildsListItemViewState>> = MutableLiveData(),
    val projectName: MutableLiveData<String> = MutableLiveData(""),
    val snackBar: MutableLiveData<String> = MutableLiveData()
) {
    fun getItemBinding(viewModel: ProjectDetailsViewModel) =
        ItemBinding.of<BuildsListItemViewState>(BR.viewState, R.layout.list_item_build)
            .bindExtra(BR.viewModel, viewModel)
}
