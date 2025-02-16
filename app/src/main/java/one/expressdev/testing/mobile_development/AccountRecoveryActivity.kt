package one.expressdev.testing.mobile_development

import EmailService
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import one.expressdev.testing.mobile_development.modelo.User
import one.expressdev.testing.mobile_development.ui.theme.Testsmobile_developmentTheme

import one.expressdev.testing.mobile_development.ui.theme.ColorScheme

class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Testsmobile_developmentTheme {
                RecoverAccount()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun RecoverAccount() {
    val context = LocalContext.current
    val email = remember { mutableStateOf("") }
    val isEmailValid = remember { mutableStateOf(true) }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val colorScheme = ColorScheme() // Create an instance of ColorScheme

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            MediumTopAppBar(
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = colorScheme.primary // Use primary color
                ),
                title = {
                    Text(
                        "Recupera tu cuenta",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = colorScheme.white, // Use white color for text
                        modifier = Modifier
                            .scale(Utils().getScale("title"))
                            .padding(start = 30.dp)
                    )
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Por favor, ingresa tu correo electrónico a continuación y te enviaremos tu contraseña.",
                modifier = Modifier.padding(bottom = 20.dp),
                color = colorScheme.white // Use white color for text
            )

            TextField(
                modifier = Modifier
                    .scale(Utils().getScale("field"))
                    .padding(vertical = 8.dp),
                value = email.value,
                onValueChange = { currentEmail ->
                    email.value = currentEmail
                    isEmailValid.value = Utils().isEmailValid(currentEmail)
                },
                label = { Text("Correo electrónico", color = colorScheme.white) }, // Use white color for label
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Email
                ),
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = colorScheme.secondary,
                    unfocusedIndicatorColor = colorScheme.tertiary,
                    containerColor = colorScheme.primary,
                    cursorColor = colorScheme.white, // Use white color for cursor
                    unfocusedPlaceholderColor = colorScheme.white.copy(alpha = 0.5f)
                )
            )

            Button(
                onClick = {
                    if (isEmailValid.value) {
                        val users = User.getUsers()
                        val email = email.value
                        val user = users.firstOrNull { it.email == email }
                        if (user != null) {
                            EmailService().sendEmail(email, user.password)
                        }
                        val intent = Intent(context, LoginActivity::class.java)
                        context.startActivity(intent)
                    } else {
                        android.widget.Toast.makeText(
                            context,
                            "Error: Por favor, ingresa un correo electrónico válido.",
                            android.widget.Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                modifier = Modifier.padding(innerPadding),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorScheme.quaternary, // Use quaternary color for button background
                    contentColor = colorScheme.white // Use white color for button text
                )
            ) {
                Text("Recuperar cuenta") // The text color is set by contentColor
            }
        }
    }
}
