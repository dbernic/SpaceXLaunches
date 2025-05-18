package com.dbernic.spacexlaunches.model.datasources.rest

import com.dbernic.spacexlaunches.model.entities.rest.LaunchData
import com.dbernic.spacexlaunches.model.entities.rest.PayloadResponse
import com.dbernic.spacexlaunches.model.entities.rest.RocketResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface HttpInterface {

    @GET("launches")
    suspend fun getLaunches(): Response<ArrayList<LaunchData>>

    @GET("rockets/{rocketId}")
    suspend fun getRocket(
        @Path("rocketId") rocketId: String,
    ): Response<RocketResponse>

    @GET("payloads/{payloadId}")
    suspend fun getPayload(
        @Path("payloadId") payloadId: String,
    ): Response<PayloadResponse>
}