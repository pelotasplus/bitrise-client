package pl.pelotasplus.bitrise.features.project_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import pl.pelotasplus.bitrise.databinding.FragmentProjectDetailsBinding
import pl.pelotasplus.bitrise.extensions.appComponent
import pl.pelotasplus.bitrise.extensions.viewModels

class ProjectListFragment : Fragment() {

    private val projectDetailsViewModel by viewModels {
        appComponent.projectDetailsViewModelFactory
            .create(navArgs<ProjectListFragmentArgs>().value.project)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentProjectDetailsBinding
        .inflate(layoutInflater, container, false)
        .apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = projectDetailsViewModel
            viewState = projectDetailsViewModel.viewState
        }
        .apply {
            buildsList.addItemDecoration(
                DividerItemDecoration(requireContext(), RecyclerView.VERTICAL)
            )
        }
        .also {
            projectDetailsViewModel.viewState.snackBar.observe(viewLifecycleOwner) { message ->
                Snackbar.make(it.root, message, Snackbar.LENGTH_LONG).show()
            }
        }
        .root
}