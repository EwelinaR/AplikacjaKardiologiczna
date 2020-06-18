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
    fun toTimestamp(value: Long) = Date(value)    // Long to Date

    @TypeConverter
    fun fromTimestamp(date: Date) = date.time   // Date to Long

}
