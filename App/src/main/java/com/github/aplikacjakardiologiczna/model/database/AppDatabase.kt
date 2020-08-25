package com.github.aplikacjakardiologiczna.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.github.aplikacjakardiologiczna.model.database.converter.Converter
import com.github.aplikacjakardiologiczna.model.database.dao.GroupDao
import com.github.aplikacjakardiologiczna.model.database.dao.TaskDao
import com.github.aplikacjakardiologiczna.model.database.dao.UserTaskDao
import com.github.aplikacjakardiologiczna.model.database.dao.UserTaskDetailsDao
import com.github.aplikacjakardiologiczna.model.database.entity.Group
import com.github.aplikacjakardiologiczna.model.database.entity.GroupTask
import com.github.aplikacjakardiologiczna.model.database.entity.Task
import com.github.aplikacjakardiologiczna.model.database.entity.UserTask


@Database(entities = [Task::class, UserTask::class, Group::class, GroupTask::class], version = 1)
@TypeConverters(Converter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao
    abstract fun userTaskDao(): UserTaskDao
    abstract fun userTaskDetailsDao(): UserTaskDetailsDao
    abstract fun groupDao(): GroupDao

    companion object {
        private var INSTANCE: AppDatabase? = null
        private const val DATABASE_NAME = "cardio_app_database"
        private const val DATABASE_INITIAL = "database/pre_populated.db"

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                DATABASE_NAME
            )
//                .fallbackToDestructiveMigration() Add it back and build app to apply changes in pre-populated.db
                .createFromAsset(DATABASE_INITIAL)
                .build()
                .apply { INSTANCE = this }
        }
    }
}
