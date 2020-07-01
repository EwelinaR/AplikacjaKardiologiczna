package com.github.aplikacjakardiologiczna.model.database.converter

import androidx.room.TypeConverter
import com.github.aplikacjakardiologiczna.model.database.Category
import java.util.Date

class Converter {

    @TypeConverter
    fun toCategory(value: Int) = enumValues<Category>()[value]

    @TypeConverter
    fun fromCategory(value: Category) = value.ordinal

    @TypeConverter
    fun toTimestamp(value: Long?): Date? = value?.let { Date(it) }

    @TypeConverter
    fun fromTimestamp(date: Date?): Long? = date?.time
}
