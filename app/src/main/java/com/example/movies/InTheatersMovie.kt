package com.example.movies

import com.google.gson.annotations.SerializedName

/**
 * The Model for storing a single movie from the TMDb API
 *
 * SerializedName tags MUST match the JSON response for the
 * object to correctly parse with the gson library.
 */

data class InTheatersMovie(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String?,
    @SerializedName("overview") val overview: String?,
    @SerializedName("poster_path") val posterPath: String?,
    // Add any other fields that match the JSON structure here
)