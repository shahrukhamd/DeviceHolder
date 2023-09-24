package com.shahrukhamd.deviceholder.di

import com.shahrukhamd.deviceholder.BuildConfig
import com.shahrukhamd.deviceholder.data.api.PatronusService
import com.shahrukhamd.deviceholder.data.repository.CustomerRepository
import com.shahrukhamd.deviceholder.data.repository.CustomerRepositoryImpl
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    fun provideConverterFactory(): Converter.Factory {
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
        return MoshiConverterFactory.create(moshi)
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        converterFactory: Converter.Factory
    ): Retrofit.Builder {
        return Retrofit.Builder()
            .addConverterFactory(converterFactory)
            .client(okHttpClient)
    }

    @Provides
    @Singleton
    fun providePatronusService(retrofitBuilder: Retrofit.Builder): PatronusService {
        return retrofitBuilder
            .baseUrl("https://api.code-challenge.patronus-group.com/") // fixme url shouldn't be hardcoded
            .build()
            .create(PatronusService::class.java)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideCustomerRepository(patronusService: PatronusService): CustomerRepository = CustomerRepositoryImpl(patronusService)
}