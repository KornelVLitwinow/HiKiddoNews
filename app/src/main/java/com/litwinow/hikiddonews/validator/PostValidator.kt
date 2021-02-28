package com.litwinow.hikiddonews.validator

import androidx.annotation.StringRes
import com.litwinow.hikiddonews.R
import com.litwinow.hikiddonews.validator.PostValidator.ValidationState.*
import javax.inject.Inject

class PostValidator @Inject constructor() {


    fun validateFields(
        header: String,
        description: String,
    ): Map<InputTypeValidation, ValidationState> {
        val headerState = checkFieldIsEmpty(header)
        val descriptionState = checkFieldIsEmpty(description)

        return mapOf(
            InputTypeValidation.HEADER to headerState,
            InputTypeValidation.DESCRIPTION to descriptionState,
        )
    }

    fun isValid(validationState: Map<InputTypeValidation, ValidationState>): Boolean {
        for (key in validationState.keys) {
            if (validationState[key] is Invalid) {
                return false
            }
        }
        return true
    }

    private fun checkFieldIsEmpty(text: String): ValidationState {
        if (text.isEmpty()) {
            return Invalid(R.string.error_empty_field)
        }
        return Valid
    }

    sealed class ValidationState {
        object Valid : ValidationState()
        data class Invalid(@StringRes val errorMessage: Int) : ValidationState()
    }

    enum class InputTypeValidation {
        HEADER,
        DESCRIPTION
    }
}