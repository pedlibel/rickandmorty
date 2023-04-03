package com.rickandmorty.presentation.common.errormapper

import com.google.gson.Gson
import com.rickandmorty.data.db.ws.model.response.RickApiErrorResponse
import com.rickandmorty.entity.ServiceException
import okhttp3.ResponseBody
import retrofit2.Response

class ErrorMapper {
    companion object {
        fun errorHandler(response: Response<*>) =
            ServiceException(
                response.code(),
                getRickApiErrorResponse(response.errorBody()),
            )

        private fun getRickApiErrorResponse(responseBody: ResponseBody?) =
            responseBody?.string()?.let {
                Gson().fromJson(it, RickApiErrorResponse::class.java).error
            } ?: ""
    }
}
