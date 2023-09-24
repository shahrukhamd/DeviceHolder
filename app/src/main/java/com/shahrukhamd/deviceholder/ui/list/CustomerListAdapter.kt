package com.shahrukhamd.deviceholder.ui.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.shahrukhamd.deviceholder.R
import com.shahrukhamd.deviceholder.data.model.Customer
import com.shahrukhamd.deviceholder.databinding.ViewCustomerListItemBinding

class CustomerListAdapter(
    private val viewModel: CustomerViewModel
) : ListAdapter<Customer, CustomerListAdapter.ViewHolder>(CustomerListDiffUtil()) {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding: ViewCustomerListItemBinding? = DataBindingUtil.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_customer_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.binding?.also {
            it.customer = item
            it.viewModel = viewModel
        }
    }
}

class CustomerListDiffUtil : DiffUtil.ItemCallback<Customer>() {
    override fun areItemsTheSame(oldItem: Customer, newItem: Customer): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Customer, newItem: Customer): Boolean {
        return oldItem == newItem
    }
}