package com.example.mytestproject.DB

import androidx.room.*
import com.example.mytestproject.Model.ModelCatFavourites

@Dao
interface ReadoutModelDao {
   /* @get:Query("select * from ModelCatFavourites")
    val allReadoutItems: List<ModelCatFavourites>*/

    @Query("SELECT * FROM ModelCatFavourites")
    fun getCats(): List<ModelCatFavourites>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addReadout(vararg todo: ModelCatFavourites)

    @Update
    fun updateReadout(readoutModel: ModelCatFavourites)

    @Delete
    fun deleteReadout(readoutModel: ModelCatFavourites)

    @Query("DELETE FROM ModelCatFavourites")
    fun nukeTable()
}