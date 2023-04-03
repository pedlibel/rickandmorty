package com.rickandmorty.data.db.ws.model.response

import com.google.gson.annotations.SerializedName

data class RickApiErrorResponse(
    @SerializedName("error")
    val error: String,
)
