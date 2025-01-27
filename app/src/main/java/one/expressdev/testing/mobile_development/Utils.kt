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
            { if (it.length < 8) Pair(false, "La contraseña debe tener al menos 8 caracteres.") else null },
            { if (!it.any { char -> char.isUpperCase() }) Pair(false, "La contraseña debe contener al menos una letra mayúscula.") else null },
            { if (!it.any { char -> char.isLowerCase() }) Pair(false, "La contraseña debe contener al menos una letra minúscula.") else null },
            { if (!it.any { char -> char.isDigit() }) Pair(false, "La contraseña debe contener al menos un dígito.") else null },
            { if (!it.any { char -> char in "!@#$%^&*()_+{}[]|\\:;\"'<,>.?/" }) Pair(false, "La contraseña debe contener al menos un carácter especial.") else null }
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
