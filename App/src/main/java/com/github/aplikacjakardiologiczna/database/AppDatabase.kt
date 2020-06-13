package com.github.aplikacjakardiologiczna.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters


@Database(entities = arrayOf(Task::class), version = 1)
@TypeConverters(Converter::class)

abstract class AppDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao

    companion object{
        private var INSTANCE: AppDatabase? = null
        fun getInstance(context: Context): AppDatabase{
            if (INSTANCE == null){
                INSTANCE = Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "CardioAppDatabase")
                    .build()
            }

            return INSTANCE as AppDatabase
        }
    }
}
