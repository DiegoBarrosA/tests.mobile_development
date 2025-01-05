package one.expressdev.testing.mobile_development

import EmailService
import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import one.expressdev.testing.mobile_development.modelo.User
import one.expressdev.testing.mobile_development.ui.theme.Testsmobile_developmentTheme

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
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            MediumTopAppBar(
                title = {
                    Text(
                        "Recover your account",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .scale(Utils().getScale("title"))
                            .padding(start = 30.dp)

                    )
                },

                scrollBehavior = scrollBehavior
            )
        }
    ) { innerPadding ->


        Column(   modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally) {
Text("Please enter your email below and we will send  you your password.", modifier = Modifier.padding(bottom = 20.dp))

            TextField(
                modifier = Modifier

                    .scale(Utils().getScale("field"))
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
            Button(onClick = {
                if(isEmailValid.value){

                    val users = User.getUsers()
                    val email = email.value
                    val user = users.firstOrNull { it.email == email }
                    if(user != null)
                    {
                        EmailService().sendEmail(email,user.password)
                    }
                    else{
                        Log.d("AccountRecoveryActivity","null user")
                    }

                    val intent = Intent(context, LoginActivity::class.java)
                    context.startActivity(intent)

                }
                else{

                    android.widget.Toast.makeText(
                        context,
                        "Error: Please enter a valid email.",
                        android.widget.Toast.LENGTH_SHORT
                    ).show()

                }

            },
                modifier = Modifier.padding(innerPadding)
            )
            {


                Text("Recover account")
            }

        }

    }
}