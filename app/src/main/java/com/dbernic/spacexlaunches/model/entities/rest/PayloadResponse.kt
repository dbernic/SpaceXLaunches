package com.dbernic.spacexlaunches.model.entities.rest

import com.google.gson.annotations.SerializedName

data class PayloadResponse(
    @SerializedName("mass_kg")
    val mass: Int?,
)
