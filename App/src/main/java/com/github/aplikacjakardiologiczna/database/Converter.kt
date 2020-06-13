package com.github.aplikacjakardiologiczna.database

import androidx.room.TypeConverter

class Converter {

    @TypeConverter
    fun toCategory(value: Int) = enumValues<Category>()[value]  // Int to Category

    @TypeConverter
    fun fromCategory(value: Category) = value.ordinal   // Category to Int

}