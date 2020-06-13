package com.github.aplikacjakardiologiczna.model.database

import com.github.aplikacjakardiologiczna.R

enum class Category(val stringResourceId: Int, val categoryIcon: Int) {
    EXERCISE(R.string.category_exercise, R.drawable.ic_bike),
    FOOD(R.string.category_food, R.drawable.ic_fitness_center),
    MEDICINE(R.string.category_medicine, R.drawable.ic_run),
    PRESSURE(R.string.category_pressure, R.drawable.ic_pool)
}
