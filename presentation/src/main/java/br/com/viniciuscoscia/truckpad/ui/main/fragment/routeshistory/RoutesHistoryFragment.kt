package br.com.viniciuscoscia.truckpad.ui.main.fragment.routeshistory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.viniciuscoscia.truckpad.R
import kotlinx.android.synthetic.main.fragment_routes_history.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class RoutesHistoryFragment : Fragment() {

    private val viewModel: RoutesHistoryViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = inflater.inflate(R.layout.fragment_routes_history, container, false)!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
    }

    private fun setupRecyclerView() = with(rvRoutesHistory) {
        setHasFixedSize(true)
        layoutManager = LinearLayoutManager(requireContext())

        val routesHistoryAdapter = RoutesHistoryAdapter { calcResults ->
            val action =
                RoutesHistoryFragmentDirections.actionNavigationRoutesHistoryToRouteResultsFragment(
                    calcResults
                )
            requireView().findNavController().navigate(action)
        }

        adapter = routesHistoryAdapter

        viewModel.getCalcResultsHistory().observe(viewLifecycleOwner, Observer {
            if (it.isEmpty()) {
                showNoItensFoundLabel(true)
                return@Observer
            }

            showNoItensFoundLabel(false)
            routesHistoryAdapter.calcResults = it
        })
    }

    private fun showNoItensFoundLabel(show: Boolean) {
        tvNoCalcsHistory.visibility = if (show) View.VISIBLE else View.INVISIBLE
        rvRoutesHistory.visibility = if (show) View.INVISIBLE else View.VISIBLE
    }
}
