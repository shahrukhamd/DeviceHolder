package com.shahrukhamd.deviceholder.data.model

data class ApiResponseWrapper<T>(
    val responseData: T? = null,
    val code: Int,
    val message: String? = null
)