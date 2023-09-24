package com.shahrukhamd.deviceholder.ui.list

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shahrukhamd.deviceholder.data.model.Customer
import com.shahrukhamd.deviceholder.data.repository.CustomerRepository
import com.shahrukhamd.deviceholder.ui.base.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import java.net.HttpURLConnection
import javax.inject.Inject

@HiltViewModel
class CustomerViewModel @Inject constructor(
    private val customerRepository: CustomerRepository
) : ViewModel() {

    private val _showCustomer = MutableLiveData<List<Customer>>()
    val showCustomer: LiveData<List<Customer>> = _showCustomer

    private val _showCustomerDetails = MutableLiveData<Event<Customer>>()
    val showCustomerDetails: LiveData<Event<Customer>> = _showCustomerDetails

    private val _showError = MutableLiveData<Event<String>>()
    val showError: LiveData<Event<String>> = _showError

    private val _showLoading = MutableLiveData<Event<Boolean>>()
    val showLoading: LiveData<Event<Boolean>> = _showLoading

    private val _closeCustomerDetails = MutableLiveData<Event<Boolean>>()
    val closeCustomerDetails: LiveData<Event<Boolean>> = _closeCustomerDetails

    init {
        loadCustomers()
    }

    fun onCustomerListRefresh() {
        loadCustomers()
    }

    fun onCustomerItemClicked(customer: Customer) {
        customer.id?.let {
            getCustomerDetails(it)
        }
    }

    fun onCustomerDetailBackPress() {
        _closeCustomerDetails.postValue(Event(true))
    }

    private fun loadCustomers() {
        viewModelScope.launch(Dispatchers.IO) {
            customerRepository.getCustomers()
                .flowOn(Dispatchers.IO)
                .onStart {
                    _showLoading.postValue(Event(true))
                }.onCompletion {
                    _showLoading.postValue(Event(false))
                }.catch { e ->
                    _showError.postValue(Event(e.message.orEmpty()))
                    Log.e(TAG, "loadCustomerList: ", e)
                }.collect {
                    if (it.code == HttpURLConnection.HTTP_OK && it.responseData != null) {
                        _showCustomer.postValue(it.responseData!!)
                    } else {
                        _showError.postValue(Event("[${it.code}] ${it.message}"))
                    }
                }
        }
    }

    private fun getCustomerDetails(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            customerRepository.getCustomerDetail(id)
                .flowOn(Dispatchers.IO)
                .catch { e ->
                    _showError.postValue(Event(e.message.orEmpty()))
                    Log.e(TAG, "loadCustomerDetails: ", e)
                }.collect {
                    if (it.code == HttpURLConnection.HTTP_OK && it.responseData != null) {
                        _showCustomerDetails.postValue(Event(it.responseData))
                    } else {
                        _showError.postValue(Event("[${it.code}] ${it.message}"))
                    }
                }
        }
    }

    companion object {
        val TAG = CustomerViewModel::class.simpleName
    }
}