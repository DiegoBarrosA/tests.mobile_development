package one.expressdev.testing.mobile_development

class Utils {
    fun isEmailValid(email: String): Boolean {
        val emailRegex = Regex("^[A-Za-z](.*)([@])(.+)(\\.)(.+)")
        return emailRegex.matches(email)
    }
    fun validatePassword(password: String): Pair<Boolean, String> {
        // Check if the password is at least 8 characters long
        if (password.length < 8) {
            return Pair(false, "Password must be at least 8 characters long.")
        }

        // Check if the password contains at least one uppercase letter
        if (!password.any { it.isUpperCase() }) {
            return Pair(false, "Password must contain at least one uppercase letter.")
        }

        // Check if the password contains at least one lowercase letter
        if (!password.any { it.isLowerCase() }) {
            return Pair(false, "Password must contain at least one lowercase letter.")
        }

        // Check if the password contains at least one digit
        if (!password.any { it.isDigit() }) {
            return Pair(false, "Password must contain at least one digit.")
        }

        // Check if the password contains at least one special character
        val specialChars = "!@#$%^&*()_+{}[]|\\:;\"'<,>.?/"
        if (!password.any { it in specialChars }) {
            return Pair(false, "Password must contain at least one special character.")
        }

        // If all checks pass, the password is considered secure
        return Pair(true, "")
    }

    }