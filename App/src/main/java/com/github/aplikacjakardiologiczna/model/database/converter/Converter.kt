package com.github.aplikacjakardiologiczna.model.database.converter

import androidx.room.TypeConverter
import com.github.aplikacjakardiologiczna.model.database.Category
import java.util.*

class Converter {

    @TypeConverter
    fun toCategory(value: Int) = enumValues<Category>()[value]  // Int to Category

    @TypeConverter
    fun fromCategory(value: Category) = value.ordinal   // Category to Int

    @TypeConverter
    fun toTimestamp(value: Long): Date? {    // Long to Date
        return if (value > 0) Date(value)
        else return null
    }

    @TypeConverter
    fun fromTimestamp(date: Date?): Long {    // Date to Long
        return date?.time ?: 0
    }

}
