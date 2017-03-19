package com.app.backpackr.data.network.services

import com.app.backpackr.data.network.models.BasePlaceDTO
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by konstie on 01.01.17.
 */

interface PlacesRetrievalService {
    @GET("nearbysearch/json")
    fun getAllPlacesByCriteria(
            @Query("location") coordsString: String,
            @Query("radius") radius: Int,
            @Query("keyword") keyword: List<String>,
            @Query("key") apiKey: String
    ): Observable<BasePlaceDTO>
}