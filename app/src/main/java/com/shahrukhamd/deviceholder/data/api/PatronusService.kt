package com.shahrukhamd.deviceholder.data.api

import com.shahrukhamd.deviceholder.data.model.Customer
import com.shahrukhamd.deviceholder.data.model.DeviceHolderResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface PatronusService {

    @GET("/users")
    suspend fun getDeviceHolders(): Response<DeviceHolderResponse>

    @GET("/users/{id}")
    suspend fun getDeviceHolderDetails(@Path("id") id: Int): Response<Customer>
}