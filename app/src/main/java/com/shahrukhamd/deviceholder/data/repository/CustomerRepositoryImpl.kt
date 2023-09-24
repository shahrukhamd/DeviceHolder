package com.shahrukhamd.deviceholder.data.repository

import com.shahrukhamd.deviceholder.data.api.PatronusService
import com.shahrukhamd.deviceholder.data.model.ApiResponseWrapper
import com.shahrukhamd.deviceholder.data.model.Customer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CustomerRepositoryImpl @Inject constructor(
    private val patronusService: PatronusService
) : CustomerRepository {

    override suspend fun getCustomers(): Flow<ApiResponseWrapper<List<Customer>>> =
        flow {
            patronusService.getDeviceHolders().let {
                if (it.isSuccessful) {
                    emit(
                        ApiResponseWrapper(
                            it.body()?.customers?.filterNotNull(),
                            it.code(),
                            it.message()
                        )
                    )
                } else {
                    emit(ApiResponseWrapper(null, it.code(), it.message()))
                }
            }
        }

    override suspend fun getCustomerDetail(id: Int): Flow<ApiResponseWrapper<Customer>> =
        flow {
            patronusService.getDeviceHolderDetails(id).let {
                if (it.isSuccessful) {
                    emit(ApiResponseWrapper(it.body(), it.code(), it.message()))
                } else {
                    emit(ApiResponseWrapper(null, it.code(), it.message()))
                }
            }
        }
}