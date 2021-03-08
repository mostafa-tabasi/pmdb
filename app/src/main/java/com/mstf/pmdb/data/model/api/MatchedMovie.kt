package com.mstf.pmdb.data.model.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class MatchedMovie(
  @Expose @SerializedName("Title") val title: String?,
  @Expose @SerializedName("Year") val year: String?,
  @Expose @SerializedName("Rated") val rated: String?,
  @Expose @SerializedName("Released") val released: String?,
  @Expose @SerializedName("Runtime") val runtime: String?,
  @Expose @SerializedName("Genre") val genre: String?,
  @Expose @SerializedName("Director") val director: String?,
  @Expose @SerializedName("Writer") val writer: String?,
  @Expose @SerializedName("Actors") val actors: String?,
  @Expose @SerializedName("Plot") val plot: String?,
  @Expose @SerializedName("Language") val language: String?,
  @Expose @SerializedName("Country") val country: String?,
  @Expose @SerializedName("Awards") val awards: String?,
  @Expose @SerializedName("Poster") val poster: String?,
  @Expose @SerializedName("Ratings") val ratings: List<Ratings>?,
  @Expose @SerializedName("Metascore") val metascore: String?,
  @Expose @SerializedName("imdbRating") val imdbRating: String?,
  @Expose @SerializedName("imdbVotes") val imdbVotes: String?,
  @Expose @SerializedName("imdbID") val imdbID: String?,
  @Expose @SerializedName("Type") val type: String?,
  @Expose @SerializedName("DVD") val dvd: String?,
  @Expose @SerializedName("BoxOffice") val boxOffice: String?,
  @Expose @SerializedName("Production") val production: String?,
  @Expose @SerializedName("Website") val website: String?,
  @Expose @SerializedName("Error") val error: String?,
  @Expose @SerializedName("Response") val response: Boolean
)
