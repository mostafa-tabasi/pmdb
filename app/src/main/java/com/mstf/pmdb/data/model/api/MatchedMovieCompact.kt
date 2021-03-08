package com.mstf.pmdb.data.model.api

import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Keep
data class MatchedMovieCompact(
  @Expose @SerializedName("Title") val title: String?,
  @Expose @SerializedName("Year") val year: String?,
  @Expose @SerializedName("imdbID") val imdbID: String?,
  @Expose @SerializedName("Type") val type: String?,
  @Expose @SerializedName("Poster") val poster: String?
)