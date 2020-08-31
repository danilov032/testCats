package com.example.mytestproject

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.mytestproject.DB.ReadoutModelDao
import com.example.mytestproject.Model.ModelCatFavourites

@Database(entities = arrayOf(ModelCatFavourites::class),version = 1)
abstract class dbAbstract: RoomDatabase(){
    abstract fun catsDao(): ReadoutModelDao

    companion object {
        var INSTANCE: dbAbstract? = null

        fun getDatabase(context: Context): dbAbstract? {
            if (INSTANCE == null){
                INSTANCE = Room.databaseBuilder(context.applicationContext, dbAbstract::class.java, "myDB").build()}
            return INSTANCE
        }
        fun destroyDataBase(){
            INSTANCE = null
        }
    }
}