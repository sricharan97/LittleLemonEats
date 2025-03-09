package com.littlelemon.littlelemonapp.utils


class Validator {

    fun validateName(name : String) : Result<String> {
        return when {
            name.isEmpty() -> Result.failure(Exception("First name is required"))
            name.length < 2 -> Result.failure(Exception("First name must be at least 2 characters long"))
            !name.all { it.isLetter() || it.isWhitespace() } -> Result.failure(Exception("First name can only contain letters"))
            else -> Result.success(name)
        }
    }


    fun validateEmail(email: String): Result<String> {

        val emailRegex = "[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}"
        return when {
            email.isEmpty() -> Result.failure(Exception("Email is required"))
            !email.matches(emailRegex.toRegex()) -> Result.failure(Exception("Invalid email format"))
            else -> Result.success(email)
        }
    }
}


