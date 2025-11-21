package com.example.ad_display.model
import com.google.gson.annotations.SerializedName

data class AdResponse(
    val username: String,
    val watched: Boolean,
    @SerializedName("video_url") val videoUrl: String
)
