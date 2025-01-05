package one.expressdev.testing.mobile_development

import android.util.Log
import java.util.Properties
import javax.mail.Message
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage


import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale

class Utils {
    fun getScale(elementType: String): Float {
        var scale: Float = 1.5f;
        scale = if (elementType == "button") {
            1.6f
        } else if (elementType == "field") {
            1.1f
        } else if (elementType == "title") {
            1.2f
        } else {
            1.5f
        }
        return scale;
    }

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