package com.dbernic.spacexlaunches.model.entities.ui

data class LaunchDetailsUi(
    val id: String,
    val name: String,
    val description: String?,
    val launchDate: String,
    val rocketName: String,
    val payloadWeight: String,
    val youTubeURL: String?,
    val imageURL: String?,
    val wikiURL: String?,
    val isFavorite: Boolean,
)