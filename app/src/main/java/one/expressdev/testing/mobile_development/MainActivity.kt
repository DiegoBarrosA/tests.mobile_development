package one.expressdev.testing.mobile_development

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import one.expressdev.testing.mobile_development.modelo.User
import one.expressdev.testing.mobile_development.ui.theme.Testsmobile_developmentTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Testsmobile_developmentTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android", modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    val context = LocalContext.current;
    Column {
        Text(
            text = "Hello, MainActivity $name!", modifier = modifier
        )
        Button(
            onClick = {
                val intent = Intent(context, HomeActivity::class.java)
                context.startActivity(intent)
            }, modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "Ir a HomeActivity")
        }
        FormularioUsuario()
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Testsmobile_developmentTheme {
        FormularioUsuario()
    }

}

@Composable
fun FormularioUsuario() {
    val nombres = remember { mutableStateOf("") }
    val apellidos = remember { mutableStateOf("") }
    val correo = remember { mutableStateOf("") }
    val context = LocalContext.current;

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)


    ) {
        TextField(
            value = nombres.value, onValueChange = { nombres.value = it },
            label = { Text("Nombres") }
        )
        TextField(
            value = apellidos.value, onValueChange = { apellidos.value = it },
            label = { Text("Apellidos") }
        )
        TextField(
            value = correo.value, onValueChange = { correo.value = it },
            label = { Text("Correo") }
        )
        Button(onClick = {
            val user = User(nombres.value, apellidos.value, correo.value)
            User.addUser(user)
            val intent = Intent(context, LoginActivity::class.java)
            context.startActivity(intent)
        }) { Text("Agregar Usuario") }
    }

}