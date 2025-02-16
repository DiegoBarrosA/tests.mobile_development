package one.expressdev.testing.mobile_development.rss
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import one.expressdev.testing.mobile_development.MainActivity
import one.expressdev.testing.mobile_development.rss.ui.theme.Testsmobile_developmentTheme
import one.expressdev.testing.mobile_development.modelo.RssFeed
import one.expressdev.testing.mobile_development.modelo.User

class RssFeedConfigActivity : ComponentActivity() {
    private val rssFeedRepository = RssFeed

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Testsmobile_developmentTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFFFFFFFF)
                ) {
                    RssFeedConfigScreen(
                        repository = rssFeedRepository,
                        onAddFeed = { feed -> rssFeedRepository.addFeed(feed) },
                        onRemoveFeed = { feed -> rssFeedRepository.removeFeed(feed) },
                        onLogout = {
                            User.setLoggedUser(null)
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RssFeedConfigScreen(
    repository: RssFeed.Companion,
    onAddFeed: (RssFeed) -> Unit,
    onRemoveFeed: (RssFeed) -> Unit,
    onLogout: () -> Unit
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var showProfileDialog by remember { mutableStateOf(false) }
    var newFeedName by remember { mutableStateOf("") }
    var newFeedUrl by remember { mutableStateOf("") }

    val currentUser = User.getLoggedUser()
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var currentPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(showProfileDialog) {
        if (showProfileDialog && currentUser is User) {
            firstName = currentUser.first_name
            lastName = currentUser.last_name
            email = currentUser.email
            currentPassword = ""
            errorMessage = null
        }
    }

    Scaffold(
        containerColor = Color(0xFFFFFFFF),
        topBar = {
            SmallTopAppBar(
                title = {
                    Text(
                        "Configuración de RSS Feed",
                        color = Color(0xFF175770)
                    )
                },
                actions = {
                    IconButton(onClick = { showProfileDialog = true }) {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = "Actualizar Perfil",
                            tint = Color(0xFF43409B)
                        )
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = Color(0xFFFFFFFF),
                    titleContentColor = Color(0xFF175770),
                    actionIconContentColor = Color(0xFF43409B)
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = Color(0xFF175770)
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Agregar Feed",
                    tint = Color(0xFFFFFFFF)
                )
            }
        }
    ) { padding ->
        Column(Modifier.padding(padding)) {
            Button(
                onClick = { onLogout() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF175770),
                    contentColor = Color(0xFFFFFFFF)
                )
            ) {
                Text("Cerrar Sesión")
            }

            LazyColumn {
                items(repository.getFeeds()) { feed ->
                    RssFeedItem(feed = feed, onDelete = { onRemoveFeed(feed) })
                }
            }

            if (showAddDialog) {
                AlertDialog(
                    onDismissRequest = { showAddDialog = false },
                    containerColor = Color(0xFFFFFFFF),
                    titleContentColor = Color(0xFF175770),
                    title = { Text("Agregar Nuevo RSS Feed") },
                    text = {
                        Column {
                            TextField(
                                value = newFeedName,
                                onValueChange = { newFeedName = it },
                                label = { Text("Nombre del Feed") },
                                colors = TextFieldDefaults.textFieldColors(
                                    cursorColor = Color(0xFF175770),
                                    focusedLabelColor = Color(0xFF175770),
                                    focusedIndicatorColor = Color(0xFF175770)
                                )
                            )
                            Spacer(Modifier.height(8.dp))
                            TextField(
                                value = newFeedUrl,
                                onValueChange = { newFeedUrl = it },
                                label = { Text("URL del Feed") },
                                colors = TextFieldDefaults.textFieldColors(
                                    cursorColor = Color(0xFF175770),
                                    focusedLabelColor = Color(0xFF175770),
                                    focusedIndicatorColor = Color(0xFF175770)
                                )
                            )
                        }
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                if (newFeedName.isNotBlank() && newFeedUrl.isNotBlank()) {
                                    onAddFeed(RssFeed(name = newFeedName, url = newFeedUrl))
                                    newFeedName = ""
                                    newFeedUrl = ""
                                    showAddDialog = false
                                }
                            },
                            colors = ButtonDefaults.textButtonColors(
                                contentColor = Color(0xFF175770)
                            )
                        ) {
                            Text("Agregar")
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = { showAddDialog = false },
                            colors = ButtonDefaults.textButtonColors(
                                contentColor = Color(0xFF254691)
                            )
                        ) {
                            Text("Cancelar")
                        }
                    }
                )
            }

            if (showProfileDialog && currentUser is User) {
                AlertDialog(
                    onDismissRequest = { showProfileDialog = false },
                    containerColor = Color(0xFFFFFFFF),
                    titleContentColor = Color(0xFF175770),
                    title = { Text("Actualizar Perfil") },
                    text = {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                        ) {
                            if (errorMessage != null) {
                                Text(
                                    text = errorMessage ?: "",
                                    color = MaterialTheme.colorScheme.error,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )
                            }
                            TextField(
                                value = firstName,
                                onValueChange = { firstName = it },
                                label = { Text("Nombre") },
                                modifier = Modifier.fillMaxWidth(),
                                colors = TextFieldDefaults.textFieldColors(
                                    cursorColor = Color(0xFF175770),
                                    focusedLabelColor = Color(0xFF175770),
                                    focusedIndicatorColor = Color(0xFF175770)
                                )
                            )
                            Spacer(Modifier.height(8.dp))
                            TextField(
                                value = lastName,
                                onValueChange = { lastName = it },
                                label = { Text("Apellido") },
                                modifier = Modifier.fillMaxWidth(),
                                colors = TextFieldDefaults.textFieldColors(
                                    cursorColor = Color(0xFF175770),
                                    focusedLabelColor = Color(0xFF175770),
                                    focusedIndicatorColor = Color(0xFF175770)
                                )
                            )
                            Spacer(Modifier.height(8.dp))
                            TextField(
                                value = email,
                                onValueChange = { email = it },
                                label = { Text("Correo Electrónico") },
                                modifier = Modifier.fillMaxWidth(),
                                colors = TextFieldDefaults.textFieldColors(
                                    cursorColor = Color(0xFF175770),
                                    focusedLabelColor = Color(0xFF175770),
                                    focusedIndicatorColor = Color(0xFF175770)
                                )
                            )
                            Spacer(Modifier.height(8.dp))
                            TextField(
                                value = currentPassword,
                                onValueChange = { currentPassword = it },
                                label = { Text("Contraseña Actual") },
                                modifier = Modifier.fillMaxWidth(),
                                visualTransformation = PasswordVisualTransformation(),
                                colors = TextFieldDefaults.textFieldColors(
                                    cursorColor = Color(0xFF175770),
                                    focusedLabelColor = Color(0xFF175770),
                                    focusedIndicatorColor = Color(0xFF175770)
                                )
                            )
                        }
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                if (firstName.isNotBlank() && lastName.isNotBlank() &&
                                    email.isNotBlank() && currentPassword.isNotBlank()
                                ) {
                                    if (currentPassword != currentUser.password) {
                                        errorMessage = "Contraseña incorrecta"
                                        return@TextButton
                                    }

                                    if (User.isEmailTaken(email, currentUser.id)) {
                                        errorMessage = "El correo ya está en uso"
                                        return@TextButton
                                    }

                                    val updatedUser = User(
                                        id = currentUser.id,
                                        first_name = firstName,
                                        last_name = lastName,
                                        email = email,
                                        password = currentUser.password
                                    )

                                    User.updateUser(updatedUser)
                                    User.setLoggedUser(updatedUser)
                                    showProfileDialog = false
                                } else {
                                    errorMessage = "Todos los campos son obligatorios"
                                }
                            },
                            colors = ButtonDefaults.textButtonColors(
                                contentColor = Color(0xFF175770)
                            )
                        ) {
                            Text("Actualizar")
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = {
                                showProfileDialog = false
                                errorMessage = null
                            },
                            colors = ButtonDefaults.textButtonColors(
                                contentColor = Color(0xFF254691)
                            )
                        ) {
                            Text("Cancelar")
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun RssFeedItem(
    feed: RssFeed,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Column(Modifier.weight(1f)) {
            Text(
                text = feed.name,
                style = MaterialTheme.typography.titleMedium,
                color = Color(0xFF175770)
            )
            Text(
                text = feed.url,
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF254691)
            )
        }
        IconButton(onClick = onDelete) {
            Icon(
                Icons.Default.Delete,
                contentDescription = "Eliminar Feed",
                tint = Color(0xFF43409B)
            )
        }
    }
}