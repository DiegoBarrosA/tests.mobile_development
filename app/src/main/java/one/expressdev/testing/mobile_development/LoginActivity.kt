package one.expressdev.testing.mobile_development

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import one.expressdev.testing.mobile_development.modelo.User
import one.expressdev.testing.mobile_development.rss.ArticleList
import one.expressdev.testing.mobile_development.ui.theme.Testsmobile_developmentTheme
import one.expressdev.testing.mobile_development.ui.theme.ColorScheme

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Testsmobile_developmentTheme {
                LogInForm()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun LogInForm() {
    val colors = ColorScheme()
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val isEmailValid = remember { mutableStateOf(true) }
    val errorLogIn = false
    val context = LocalContext.current
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            MediumTopAppBar(
                title = {
                    Text(
                        "Iniciar sesión",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .scale(Utils().getScale("title"))
                            .padding(start = 30.dp),
                        color = colors.white
                    )
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = colors.primary
                ),
                scrollBehavior = scrollBehavior
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(top = 150.dp)
                .padding(horizontal = 32.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                modifier = Modifier
                    .scale(Utils().getScale("field"))
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                value = email.value,
                onValueChange = { currentEmail ->
                    email.value = currentEmail
                    isEmailValid.value = Utils().isEmailValid(currentEmail)
                },
                label = { Text("Correo electrónico") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Email
                ),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = colors.white,
                    focusedIndicatorColor = colors.tertiary,
                    unfocusedIndicatorColor = Color.Black
                )
            )

            TextField(
                modifier = Modifier
                    .scale(Utils().getScale("field"))
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                value = password.value,
                onValueChange = { password.value = it },
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Password
                ),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = colors.white,
                    focusedIndicatorColor = colors.tertiary,
                    unfocusedIndicatorColor = Color.Black
                )
            )

            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .scale(0.9f)
                    .padding(horizontal = 20.dp)
            ) {
                if (!isEmailValid.value) {
                    Text(
                        "Por favor, ingrese una dirección de correo electrónico válida.",
                        modifier = Modifier
                            .scale(Utils().getScale("text"))
                            .padding(vertical = 10.dp),
                        color = colors.quaternary
                    )
                }
                if (errorLogIn) {
                    Text(
                        "Correo electrónico o contraseña incorrectos",
                        modifier = Modifier
                            .scale(Utils().getScale("text"))
                            .padding(vertical = 10.dp),
                        color = colors.quaternary
                    )
                }
            }
            Button(
                modifier = Modifier
                    .scale(Utils().getScale("button"))
                    .padding(30.dp),
                onClick = {
                    val emailValue = email.value
                    val passwordValue = password.value
                    val users = User.getUsers()
                    val userExists = users.any { user ->
                        user.email == emailValue && user.password == passwordValue
                    }
                    if (userExists) {
                        users.find { it.email == emailValue && it.password == passwordValue }?.let {
                            User.setLoggedUser(it)
                        }
                        val intent = Intent(context, ArticleList::class.java)
                        context.startActivity(intent)
                    } else {
                        User.setLoggedUser(null)
                        android.widget.Toast.makeText(
                            context,
                            "Correo electrónico o contraseña inválidos.",
                            android.widget.Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = colors.secondary,
                    contentColor = colors.white
                )
            ) {
                Text("Iniciar sesión")
            }
            Text(
                "¿Olvidaste tu contraseña? Haz clic aquí para recuperarla.",
                modifier = Modifier.clickable(onClick = {
                    val intent = Intent(context, HomeActivity::class.java)
                    context.startActivity(intent)
                }),
                color = colors.quinary
            )
        }
    }
}