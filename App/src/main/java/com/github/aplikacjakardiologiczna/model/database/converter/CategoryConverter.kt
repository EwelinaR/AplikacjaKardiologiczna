package com.github.aplikacjakardiologiczna.model.database.converter

import com.github.aplikacjakardiologiczna.R

object CategoryConverter {

    private val categoryMap = mapOf(
        "ćwiczenia fizyczne" to R.drawable.ic_run,
        "zdrowe odżywianie" to R.drawable.ic_food,
        "przyjmowanie leków" to R.drawable.ic_pills,
        "pomiar ciśnienia" to R.drawable.ic_pressure
    )

    fun toCategory(value: String) = categoryMap[value]
}
