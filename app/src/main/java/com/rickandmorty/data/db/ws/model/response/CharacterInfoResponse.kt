package com.rickandmorty.data.db.ws.model.response

import com.google.gson.annotations.SerializedName

data class CharacterInfoResponse(
    @SerializedName("count")
    val count: Int,
    @SerializedName("pages")
    val pages: Int,
    @SerializedName("next")
    val next: String?,
    @SerializedName("prev")
    val prev: String?,
)
