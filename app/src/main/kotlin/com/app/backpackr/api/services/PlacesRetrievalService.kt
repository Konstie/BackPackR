package com.app.backpackr.api.services

import com.app.backpackr.api.models.dto.BasePlaceDTO
import retrofit2.http.GET
import retrofit2.http.Query
import rx.Observable

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