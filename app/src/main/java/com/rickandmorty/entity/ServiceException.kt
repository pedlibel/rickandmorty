package com.rickandmorty.entity

data class ServiceException(
    val errorCode: Int,
    val errorMessage: String,
) : Exception()
