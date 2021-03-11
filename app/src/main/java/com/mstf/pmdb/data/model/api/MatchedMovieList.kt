package com.mstf.pmdb.data.model.api

import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Keep
data class MatchedMovieList(
  @Expose @SerializedName("Search") val matchedList: List<MatchedMovieCompact>?,
  @Expose @SerializedName("totalResults") val totalResults: Int,
  @Expose @SerializedName("Error") val error: String?,
  @Expose @SerializedName("Response") val response: Boolean
)