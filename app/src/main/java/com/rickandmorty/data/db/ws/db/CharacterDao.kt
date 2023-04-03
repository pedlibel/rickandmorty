package com.rickandmorty.data.db.ws.db

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.Update
import com.rickandmorty.data.db.ws.model.response.CharacterResonse

@Dao
interface CharacterDao {
    @Query("SELECT * FROM characters")
    fun getAll(): List<CharacterResonse>

    @Query("SELECT * FROM characters WHERE id BETWEEN :startId AND :endId")
    fun getCharactersInRange(startId: Int, endId: Int): List<CharacterResonse>

    @Query("SELECT * FROM characters WHERE id == :id")
    fun getCharacterWithId(id: Int): CharacterResonse?

    @Insert
    fun insert(character: CharacterResonse)

    @Update
    fun update(character: CharacterResonse)

    @Delete
    fun delete(character: CharacterResonse)
}

@Database(entities = [CharacterResonse::class], version = 1)
abstract class CharacterDatabase : RoomDatabase() {
    abstract fun characterDao(): CharacterDao
}
