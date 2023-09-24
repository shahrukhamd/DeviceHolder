package com.shahrukhamd.deviceholder.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.shahrukhamd.deviceholder.databinding.FragmentCustomerListBinding
import com.shahrukhamd.deviceholder.ui.base.EventObserver


class CustomerListFragment: Fragment() {

    private val viewModel: CustomerViewModel by activityViewModels()
    private var listAdapter: CustomerListAdapter? = null

    private lateinit var viewBinding: FragmentCustomerListBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentCustomerListBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
        }
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
        initObserver()

        viewBinding.srCustomerList.setOnRefreshListener {
            viewModel.onCustomerListRefresh()
        }
    }

    private fun initRecyclerView() {
        listAdapter = CustomerListAdapter(viewModel)
        viewBinding.rvCustomerList.adapter = listAdapter
    }

    private fun initObserver() {
        viewModel.showCustomer.observe(viewLifecycleOwner) {
            viewBinding.srCustomerList.isRefreshing = false
            viewBinding.rvCustomerList.visibility = View.VISIBLE
            listAdapter?.submitList(it)
        }

        viewModel.showError.observe(viewLifecycleOwner, EventObserver {
            viewBinding.srCustomerList.isRefreshing = false
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        })

        viewModel.showLoading.observe(viewLifecycleOwner, EventObserver {
            viewBinding.srCustomerList.isRefreshing = it
        })

        viewModel.showCustomerDetails.observe(viewLifecycleOwner, EventObserver { customer ->
            findNavController().navigate(
                CustomerListFragmentDirections.actionListFragmentToDetailsFragment(customer)
            )
        })
    }
}