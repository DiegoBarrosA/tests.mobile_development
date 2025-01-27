package one.expressdev.testing.mobile_development

import android.util.Log
import java.net.URL

class Utils {
    fun getScale(elementType: String): Float =
        when (elementType) {
            "button" -> 1.6f
            "field" -> 1.1f
            "title" -> 1.2f
            else -> 1.5f
        }

    fun isEmailValid(email: String): Boolean {
        val emailRegex = Regex("^[A-Za-z](.*)([@])(.+)(\\.)(.+)")
        return emailRegex.matches(email)
    }

    fun validatePassword(password: String): Pair<Boolean, String> {
        val validations = listOf<(String) -> Pair<Boolean, String>?>(
            { if (it.length < 8) Pair(false, "Password must be at least 8 characters long.") else null },
            { if (!it.any { char -> char.isUpperCase() }) Pair(false, "Password must contain at least one uppercase letter.") else null },
            { if (!it.any { char -> char.isLowerCase() }) Pair(false, "Password must contain at least one lowercase letter.") else null },
            { if (!it.any { char -> char.isDigit() }) Pair(false, "Password must contain at least one digit.") else null },
            { if (!it.any { char -> char in "!@#$%^&*()_+{}[]|\\:;\"'<,>.?/" }) Pair(false, "Password must contain at least one special character.") else null }
        )

        for (validation in validations) {
            validation(password)?.let { return it }
        }

        return Pair(true, "")
    }

    fun getBaseUrl(fullUrl: String): String {
        val url = URL(fullUrl)
        val protocol = url.protocol
        val host = url.host
        val port = url.port

        return "$protocol://$host${if (port != -1) ":$port" else ""}"
    }
}
