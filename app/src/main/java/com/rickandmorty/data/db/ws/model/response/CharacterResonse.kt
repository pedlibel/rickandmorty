package com.rickandmorty.data.db.ws.model.response

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.rickandmorty.entity.Character

@Entity(tableName = "Characters")
data class CharacterResonse(
    @SerializedName("id")
    @PrimaryKey
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("species")
    val species: String,
    @SerializedName("gender")
    val gender: String,
    @SerializedName("image")
    val image: String,
) {
    fun map() =
        Character(
            id,
            name,
            status,
            species,
            gender,
            image,
        )
}
