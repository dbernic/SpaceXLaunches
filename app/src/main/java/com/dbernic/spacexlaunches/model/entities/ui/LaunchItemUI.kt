package com.dbernic.spacexlaunches.model.entities.ui

data class LaunchItemUI(
    val id: String,
    val name: String,
    val launchDate: String,
    val imageURL: String?,
    val isFavorite: Boolean,
)