package com.github.aplikacjakardiologiczna.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.github.aplikacjakardiologiczna.model.database.converter.Converter
import com.github.aplikacjakardiologiczna.model.database.dao.TaskDao
import com.github.aplikacjakardiologiczna.model.database.dao.UserTaskDao
import com.github.aplikacjakardiologiczna.model.database.entity.Task
import com.github.aplikacjakardiologiczna.model.database.entity.UserTask


@Database(entities = [Task::class,UserTask::class], version = 1)
@TypeConverters(Converter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao
    abstract fun userTaskDao(): UserTaskDao

    companion object {
        private var INSTANCE: AppDatabase? = null
        private const val DATABASE_NAME = "cardio_app_database"
        private const val DATABASE_INITIAL = "database/pre_populated.db"

        fun getInstance(context: Context): AppDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                        context,
                        AppDatabase::class.java,
                        DATABASE_NAME)
                        .fallbackToDestructiveMigration()
                        .createFromAsset(DATABASE_INITIAL)
                        .build()
            }

            return INSTANCE as AppDatabase
        }
    }
}
