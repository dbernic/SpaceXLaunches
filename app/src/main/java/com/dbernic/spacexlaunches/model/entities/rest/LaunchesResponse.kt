package com.dbernic.spacexlaunches.model.entities.rest

import com.dbernic.spacexlaunches.model.entities.ui.LaunchDetailsUi
import com.dbernic.spacexlaunches.model.entities.ui.LaunchItemUI
import com.dbernic.spacexlaunches.utils.formatDateToDDMMYYYY
import com.google.gson.annotations.SerializedName

data class LaunchData(
    val id: String,
    val name: String,
    val payloads: ArrayList<String>,
    val rocket: String,
    val links: LaunchLinks,
    val details: String?,
    @SerializedName("date_utc")
    val dateUTC: String
) {
    fun toLaunchItemUI(isFavorite: Boolean): LaunchItemUI = LaunchItemUI(
        id = id,
        name = name,
        launchDate = dateUTC.formatDateToDDMMYYYY(),
        imageURL = links.flickr.getImage(),
        isFavorite = isFavorite
    )

    fun toDetailedUI(rocket: String, mass: Int, isFavorite: Boolean): LaunchDetailsUi = LaunchDetailsUi(
        id = id,
        name = name,
        launchDate = dateUTC.formatDateToDDMMYYYY(),
        rocketName = rocket,
        payloadWeight = mass.toString(),
        youTubeURL = links.webcast,
        wikiURL = links.wikipedia,
        imageURL = links.flickr.getImage(),
        isFavorite = isFavorite,
        description = details,
    )
}

data class LaunchLinks(
    val flickr: FlickrImages,
    val webcast: String?,
    @SerializedName("youtube_id")
    val youtubeId: String?,
    val wikipedia: String?,

)

data class FlickrImages(
    val original: ArrayList<String>
) {
    fun getImage(): String? {
        return if (original.size > 0) original[0] else null
    }
}