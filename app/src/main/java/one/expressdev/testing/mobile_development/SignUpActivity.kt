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
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
            value = first_name.value,
            onValueChange = { first_name.value = it },
            label = { Text("First name") })

        TextField(modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
            value = last_name.value,
            onValueChange = { last_name.value = it },
            label = { Text("Last name") })

        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            value = email.value,
            onValueChange = { currentEmail ->
                email.value = currentEmail
                isEmailValid.value = Utils().isEmailValid(currentEmail)
            },
            label = { Text("Email") },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Email
            )
        )

        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            value = password.value,
            onValueChange = { password.value = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Password
            )
        )
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            value = confirm_password.value,
            onValueChange = { confirm_password.value = it },
            label = { Text("Confirm Password") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Password
            )
        )


        if (!isEmailValid.value) {
            Text(
                "Please enter a valid email address.", modifier = Modifier.padding(vertical = 8.dp)
            )
        }
        if (password.value != confirm_password.value && (password.value != "" && confirm_password.value != "")) {
            Text(
                "The passwords do not match.", modifier = Modifier.padding(vertical = 8.dp)
            )
        } else {

            val (isSecure, errorMessage) = Utils().validatePassword(password.value)
            if (!isSecure) {
                Text(
                    errorMessage, modifier = Modifier.padding(vertical = 8.dp)
                )

            }
        }

        Button(modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp), onClick = {
            val user = User(first_name.value, last_name.value, email.value)
            User.addUser(user)
            val intent = Intent(context, HomeActivity::class.java)
            context.startActivity(intent)
        }) {
            Text("Sign up")
        }
    }
}

