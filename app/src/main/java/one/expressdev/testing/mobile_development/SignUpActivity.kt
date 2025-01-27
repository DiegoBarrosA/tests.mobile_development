package one.expressdev.testing.mobile_development

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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import one.expressdev.testing.mobile_development.modelo.User
import one.expressdev.testing.mobile_development.ui.theme.Testsmobile_developmentTheme

class SingUpActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Testsmobile_developmentTheme {
                SingUpForm()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun SingUpForm() {
    val first_name = remember { mutableStateOf("") }
    val last_name = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val confirm_password = remember { mutableStateOf("") }
    val isEmailValid = remember { mutableStateOf(true) }

    val context = LocalContext.current
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    Scaffold(modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            MediumTopAppBar(
                title = {
                    Text(
                        "Registrarse", maxLines = 1, overflow = TextOverflow.Ellipsis,
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
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(modifier = Modifier
                .scale(Utils().getScale("field"))
                .fillMaxWidth()
                .padding(vertical = 8.dp),
                value = first_name.value,
                onValueChange = { first_name.value = it },
                label = { Text("Nombre") })

            TextField(modifier = Modifier
                .scale(Utils().getScale("field"))
                .fillMaxWidth()
                .padding(vertical = 8.dp),
                value = last_name.value,
                onValueChange = { last_name.value = it },
                label = { Text("Apellido") })

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
                )
            )
            TextField(
                modifier = Modifier
                    .scale(Utils().getScale("field"))
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                value = confirm_password.value,
                onValueChange = { confirm_password.value =
                    it },
                label = { Text("Confirmar contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Password
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
                        "Por favor, ingresa una dirección de correo electrónico válida.",
                        modifier = Modifier
                            .scale(Utils().getScale("text"))
                            .padding(vertical = 10.dp)
                    )
                }
                if (password.value != "") {
                    if ((confirm_password.value != "") && (password.value != confirm_password.value)) {
                        Text(
                            "Las contraseñas no coinciden.",
                            modifier = Modifier
                                .scale(Utils().getScale("text"))
                                .padding(vertical = 10.dp)
                        )
                    }
                    val (isSecure, errorMessage) = Utils().validatePassword(password.value)
                    if (!isSecure) {
                        Text(
                            errorMessage, modifier = Modifier
                                .scale(Utils().getScale("text"))
                                .padding(vertical = 10.dp)
                        )
                    }
                }
            }
            Button(modifier = Modifier
                .scale(Utils().getScale("button"))
                .padding(30.dp), onClick = {
                if (Utils().isEmailValid(email.value) && (password.value == confirm_password.value) && Utils().validatePassword(password.value).first) {
                    val user = User(java.util.UUID.randomUUID().toString(), first_name.value, last_name.value, email.value, password.value)
                    User.addUser(user)

                    val intent = Intent(context, MainActivity::class.java)
                    context.startActivity(intent)
                } else {
                    android.widget.Toast.makeText(
                        context,
                        "Error: Por favor, ingresa los datos requeridos.",
                        android.widget.Toast.LENGTH_SHORT
                    ).show()
                }
            }) {
                Text("Registrarse")
            }
        }
    }
}
