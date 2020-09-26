package com.github.aplikacjakardiologiczna.model.database.converter

import com.github.aplikacjakardiologiczna.R

class CategoryConverter {

    companion object {
        private val categoryMap = mapOf(
            "ćwiczenia fizyczne" to R.drawable.ic_run,
            "zdrowe odżywianie" to R.drawable.ic_food,
            "przyjmowanie leków" to R.drawable.ic_run,
            "pomiar ciśnienia" to R.drawable.ic_pool
        )

        fun toCategory(value: String) = categoryMap[value]
    }
}
