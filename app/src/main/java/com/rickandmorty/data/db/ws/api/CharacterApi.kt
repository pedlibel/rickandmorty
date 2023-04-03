package com.rickandmorty.data.db.ws.api

import com.rickandmorty.data.db.ws.model.response.CharacterWrapperResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface CharacterApi {
    @GET("api/character")
    fun getCharactersFromPage(
        @Query("page") page: Int,
    ): Call<CharacterWrapperResponse>
}
