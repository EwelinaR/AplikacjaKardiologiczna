package com.github.aplikacjakardiologiczna.model

import com.github.aplikacjakardiologiczna.R

enum class Message(val stringResourceId: Int) {
    VALIDATION_ERROR_EMPTY_USERNAME(R.string.empty_username_validation_message),
    VALIDATION_ERROR_WRONG_USERNAME(R.string.wrong_username_validation_message),
    GENERIC_ERROR_MESSAGE(R.string.generic_error_message)
}
