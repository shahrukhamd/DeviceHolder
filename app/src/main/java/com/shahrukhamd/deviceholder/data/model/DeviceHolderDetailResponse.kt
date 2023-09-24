package com.shahrukhamd.deviceholder.data.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class Customer(

	@Json(name="firstName")
	val firstName: String? = null,

	@Json(name="lastName")
	val lastName: String? = null,

	@Json(name="phoneNumber")
	val phoneNumber: String? = null,

	@Json(name="address")
	val address: Address? = null,

	@Json(name="gender")
	val gender: String? = null,

	@Json(name="imageUrl")
	val imageUrl: String? = null,

	@Json(name="currentLongitude")
	val currentLongitude: Double? = null,

	@Json(name="currentLatitude")
	val currentLatitude: Double? = null,

	@Json(name="stickers")
	val stickers: List<String?>? = null,

	@Json(name="id")
	val id: Int? = null
) : Parcelable

@Parcelize
data class Address(

	@Json(name="zip")
	val zip: String? = null,

	@Json(name="country")
	val country: String? = null,

	@Json(name="city")
	val city: String? = null,

	@Json(name="street")
	val street: String? = null,

	@Json(name="state")
	val state: String? = null
) : Parcelable {
	override fun toString(): String {
		return "$street, $city, $state, $zip, $country"
	}
}
