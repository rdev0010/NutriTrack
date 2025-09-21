package com.fit2081.rhea32570988.model.api

import com.fit2081.rhea32570988.model.entity.Fruit
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Retrofit service interface that defines API endpoints for fruity vice operations.
 * Retrofit uses this interface to generate a concrete implementation for making HTTP requests to the remote API server.
 */
interface FruityViceApiService {
    /**
     * GET endpoint to retrieve the fruit details from the server.
     * @return Fruit containing the fruit details for the given fruit name from the API
     */
    @GET("api/fruit/{fruitName}")
    suspend fun getFruitDetails(@Path("fruitName") fruitName: String): Fruit
}