package pl.pelotasplus.bitrise.features.projects_list

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import pl.pelotasplus.bitrise.R
import pl.pelotasplus.bitrise.databinding.FragmentProjectsListBinding
import pl.pelotasplus.bitrise.extensions.appComponent
import pl.pelotasplus.bitrise.extensions.subscribeBy
import pl.pelotasplus.bitrise.extensions.viewModels

class ProjectsListFragment : Fragment() {

    private val projectListViewModel by viewModels {
        appComponent.projectsListViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentProjectsListBinding
        .inflate(layoutInflater, container, false)
        .apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = projectListViewModel
            viewState = projectListViewModel.viewState
        }
        .apply {
            projectsList.addItemDecoration(
                DividerItemDecoration(requireContext(), RecyclerView.VERTICAL)
            )
        }
        .also {
            projectListViewModel.navigation
                .subscribeBy(
                    viewLifecycleOwner,
                    onNext = {
                        findNavController().navigate(it.toAction())
                    }
                )
        }
        .also {
            projectListViewModel.viewState
                .filterMode
                .observe(
                    viewLifecycleOwner,
                    Observer { filterMode ->
                        if (!filterMode) {
                            (requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                                .hideSoftInputFromWindow(
                                    it.root.windowToken,
                                    InputMethodManager.HIDE_NOT_ALWAYS
                                )
                        }
                    }
                )
        }
        .also {
            setHasOptionsMenu(true)
        }
        .root

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.projects_list, menu);
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        when (item.itemId) {
            R.id.sort_by_owner, R.id.sort_by_project_name -> {
                projectListViewModel.onSortMethodChanged(item.itemId)
                true
            }
            R.id.filter -> {
                projectListViewModel.onToggleFilterMode()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
}