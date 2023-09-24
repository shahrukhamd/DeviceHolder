package com.shahrukhamd.deviceholder.data.repository

import com.shahrukhamd.deviceholder.data.model.ApiResponseWrapper
import com.shahrukhamd.deviceholder.data.model.Customer
import kotlinx.coroutines.flow.Flow

interface CustomerRepository {

    suspend fun getCustomers(): Flow<ApiResponseWrapper<List<Customer>>>

    suspend fun getCustomerDetail(id: Int): Flow<ApiResponseWrapper<Customer>>
}