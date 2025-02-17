package one.expressdev.testing.mobile_development

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SignUpActivityTest {

    @get:Rule
    val composeTestRule = createComposeRule()

       @Test
    fun testInvalidEmail() {
        // Launch the SignUpActivity
        composeTestRule.setContent {
            SignUpForm()
        }

        // Fill in the form with an invalid email
        fillSignUpForm("John", "Doe", "invalid-email", "Password123", "Password123")

        // Click the register button using the test tag
        composeTestRule.onNodeWithTag("registerButton").performClick()

        // Check for the error message about invalid email
        composeTestRule.onNodeWithText("Por favor, ingresa una dirección de correo electrónico válida.").assertIsDisplayed()
    }

    @Test
    fun testPasswordMismatch() {
        // Launch the SignUpActivity
        composeTestRule.setContent {
            SignUpForm()
        }

        // Fill in the form with mismatched passwords
        fillSignUpForm("John", "Doe", "john.doe@example.com", "Password123", "DifferentPassword")

        // Click the register button using the test tag
        composeTestRule.onNodeWithTag("registerButton").performClick()

        // Check for the error message about password mismatch
        composeTestRule.onNodeWithText("Las contraseñas no coinciden.").assertIsDisplayed()
    }

    private fun fillSignUpForm(firstName: String, lastName: String, email: String, password: String, confirmPassword: String) {
        composeTestRule.onNodeWithText("Nombre").performTextInput(firstName)
        composeTestRule.onNodeWithText("Apellido").performTextInput(lastName)
        composeTestRule.onNodeWithText("Correo electrónico").performTextInput(email)
        composeTestRule.onNodeWithText("Contraseña").performTextInput(password)
        composeTestRule.onNodeWithText("Confirmar contraseña").performTextInput(confirmPassword)
    }
}
