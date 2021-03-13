package com.mstf.pmdb.data.model.api

import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Keep
data class Ratings(
  @Expose @SerializedName("Source") val source: String,
  @Expose @SerializedName("Value") val value: String
)
