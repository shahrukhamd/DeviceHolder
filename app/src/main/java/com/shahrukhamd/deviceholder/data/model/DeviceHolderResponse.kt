package com.shahrukhamd.deviceholder.data.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class DeviceHolderResponse(

	@Json(name="customers")
	val customers: List<Customer?>? = null
) : Parcelable