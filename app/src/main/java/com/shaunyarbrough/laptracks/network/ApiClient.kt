package com.shaunyarbrough.laptracks.network

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object ApiClient {
	private const val BASE_URL = "https://lap-tracks-api.fly.dev/api/v1/"

	@Provides
	fun provideRetrofit(): Retrofit{
		return Retrofit.Builder()
			.baseUrl(BASE_URL)
			.addConverterFactory(GsonConverterFactory.create())
			.build()
	}
}