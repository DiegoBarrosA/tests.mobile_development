package one.expressdev.testing.mobile_development

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import org.junit.Rule
import org.junit.Test

class LoginActivityTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testEmailInput() {
        composeTestRule.setContent {
            LogInForm()
        }

        // Check if the email field is displayed
        composeTestRule.onNodeWithText("Correo electrónico").assertIsDisplayed()

        // Type an invalid email
        composeTestRule.onNodeWithText("Correo electrónico")
            .performTextInput("invalid-email")

        // Check for the error message
        composeTestRule.onNodeWithText("Por favor, ingrese una dirección de correo electrónico válida.")
            .assertIsDisplayed()
    }

    @Test
    fun testPasswordInput() {
        composeTestRule.setContent {
            LogInForm()
        }

        // Check if the password field is displayed
        composeTestRule.onNodeWithText("Contraseña").assertIsDisplayed()

        // Type a password
        composeTestRule.onNodeWithText("Contraseña")
            .performTextInput("password123")

        // Check if the password is masked
        composeTestRule.onNodeWithText("password123").assertDoesNotExist()
    }



    @Test
    fun testForgotPasswordClick() {
        composeTestRule.setContent {
            LogInForm()
        }

        // Click on the "¿Olvidaste tu contraseña?" text
        composeTestRule.onNodeWithText("¿Olvidaste tu contraseña? Haz clic aquí para recuperarla.")
            .performClick()

        // Verify that the intent to start HomeActivity is triggered
        // This part may require additional setup to verify the Intent
    }
}
