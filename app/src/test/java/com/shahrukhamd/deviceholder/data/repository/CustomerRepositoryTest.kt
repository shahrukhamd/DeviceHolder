package com.shahrukhamd.deviceholder.data.repository

import com.shahrukhamd.deviceholder.data.api.PatronusService
import com.shahrukhamd.deviceholder.data.model.Customer
import com.shahrukhamd.deviceholder.data.model.DeviceHolderResponse
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import okhttp3.internal.EMPTY_RESPONSE
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

@OptIn(ExperimentalCoroutinesApi::class)
class CustomerRepositoryTest {
    private val expectedCustomerList1 = """
[
    {
      "id": 1,
      "firstName": "John",
      "lastName": "Doe",
      "gender": "MALE",
      "phoneNumber": "123-456-7890",
      "imageUrl": "https://fastly.picsum.photos/id/473/200/300.jpg?hmac=WYG6etF60iOJeGoFVY1hVDMakbBRS32ZDGNkVZhF6-8",
      "stickers": [
        "Fam"
      ]
    },
    {
      "id": 2,
      "firstName": "Jane",
      "lastName": "Doe",
      "gender": "FEMALE",
      "phoneNumber": "123-456-7891",
      "imageUrl": "https://fastly.picsum.photos/id/445/400/400.jpg?hmac=CCjqlZXQQ_5kl0X6naMjQKUWSbQloDYImyB9zGFOA8M",
      "stickers": [
        "Fam",
        "Ban"
      ]
    },
    {
      "id": 3,
      "firstName": "Bob",
      "lastName": "Smith",
      "gender": "MALE",
      "phoneNumber": "123-456-7892",
      "imageUrl": null,
      "stickers": [
        "Ban"
      ]
    }
]"""

    private val expectedCustomerList2 = """
[
    {
      "id": 6,
      "firstName": "Emily",
      "lastName": "Brown",
      "gender": "FEMALE",
      "phoneNumber": "123-456-7895",
      "imageUrl": "https://fastly.picsum.photos/id/628/400/400.jpg?hmac=uQnrWHhKS3XBUnJaTHJHb7TBhQX7uZ0p1q_Y2hLnEWY",
      "stickers": [
        "Fam"
      ]
    },
    {
      "id": 7,
      "firstName": "David",
      "lastName": "Wilson",
      "gender": "MALE",
      "phoneNumber": "123-456-7896",
      "imageUrl": null,
      "stickers": [
        "Ban"
      ]
    },
    {
      "id": 8,
      "firstName": "Olivia",
      "lastName": "Garcia",
      "gender": "FEMALE",
      "phoneNumber": "123-456-7897",
      "imageUrl": "https://fastly.picsum.photos/id/650/400/400.jpg?hmac=aRSRZFa8j3OPfCyAkMVThCXyRqSK-GhyPHmkcZscOC8",
      "stickers": [
        "Fam"
      ]
    }
]"""

    @MockK
    private lateinit var patronusService: PatronusService

    private lateinit var customerRepository: CustomerRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxed = true)
        customerRepository = CustomerRepositoryImpl(patronusService)
    }

    @After
    fun teardown() {
        unmockkAll()
    }

    @Test
    fun `customer repo should return list of sample data 1 returned from patronus service`() = runTest {
        val res = Response.success<DeviceHolderResponse>(DeviceHolderResponse(getSampleResponse(expectedCustomerList1)))

        coEvery {
            patronusService.getDeviceHolders()
        } returns res

        val items = customerRepository.getCustomers().first()

        assertEquals(res.body()?.customers, items.responseData)
    }

    @Test
    fun `customer repo should not return another sample data not returned from patronus service`() = runTest {
        val res1 = Response.success<DeviceHolderResponse>(DeviceHolderResponse(getSampleResponse(expectedCustomerList1)))
        val res2 = Response.success<DeviceHolderResponse>(DeviceHolderResponse(getSampleResponse(expectedCustomerList2)))

        coEvery {
            patronusService.getDeviceHolders()
        } returns res2

        val items = customerRepository.getCustomers().first()

        assertNotEquals(res1.body()?.customers, items.responseData)
    }

    @Test
    fun `customer repo should return empty list returned from patronus service`() = runTest {
        val res = Response.success<DeviceHolderResponse>(DeviceHolderResponse(emptyList()))

        coEvery {
            patronusService.getDeviceHolders()
        } returns res

        val items = customerRepository.getCustomers().first()

        assertEquals(res.body()?.customers, items.responseData)
    }

    @Test
    fun `customer repo should return 400 when the same is returned from patronus service`() =
        runTest {
            val res = Response.error<DeviceHolderResponse>(400, EMPTY_RESPONSE)

            coEvery {
                patronusService.getDeviceHolders()
            } returns res

            val item = customerRepository.getCustomers().first()

            assertEquals(item.code, 400)
        }

    @Test
    fun `customer repo should return 200 when the same is returned from patronus service`() =
        runTest {
            val res = Response.success<DeviceHolderResponse>(DeviceHolderResponse(getSampleResponse(expectedCustomerList1)))

            coEvery {
                patronusService.getDeviceHolders()
            } returns res

            val item = customerRepository.getCustomers().first()

            assertEquals(item.code, 200)
        }

    private fun getSampleResponse(data: String): List<Customer>? {
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val type = Types.newParameterizedType(List::class.java, Customer::class.java)
        val adapter: JsonAdapter<List<Customer>> = moshi.adapter(type)
        return adapter.fromJson(data)
    }
}