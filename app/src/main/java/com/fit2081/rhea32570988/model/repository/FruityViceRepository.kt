package com.fit2081.rhea32570988.model.repository

import android.content.Context
import com.fit2081.rhea32570988.model.api.FruityViceApiService
import com.fit2081.rhea32570988.model.entity.Fruit
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FruityViceRepository(private val applicationContext: Context) {
    // Retrofit instance configured for API communication
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://www.fruityvice.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // API service instance (created by Retrofit) for making network requests to remote API
    private val apiService = retrofit.create(FruityViceApiService::class.java)

    suspend fun getFruitDetails(fruitName: String): Fruit {
        return apiService.getFruitDetails(fruitName)
    }
}