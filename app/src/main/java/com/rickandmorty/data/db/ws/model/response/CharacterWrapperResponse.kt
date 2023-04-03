package com.rickandmorty.data.db.ws.model.response

import com.google.gson.annotations.SerializedName

data class CharacterWrapperResponse(
    @SerializedName("info")
    val info: CharacterInfoResponse,
    @SerializedName("results")
    val result: List<CharacterResonse>,
)
