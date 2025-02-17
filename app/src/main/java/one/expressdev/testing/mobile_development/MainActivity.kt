
package one.expressdev.testing.mobile_development
import one.expressdev.testing.mobile_development.rss.ArticleList
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import one.expressdev.testing.mobile_development.LoginActivity
import one.expressdev.testing.mobile_development.SignUpActivity
import one.expressdev.testing.mobile_development.modelo.User
import one.expressdev.testing.mobile_development.ui.theme.ColorScheme
import one.expressdev.testing.mobile_development.ui.theme.Testsmobile_developmentTheme
import one.expressdev.testing.mobile_development.R

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        if (User.getLoggedUser() != null) {
            val intent = Intent(this, ArticleList::class.java)
            startActivity(intent)
            finish()
            return
        }

        setContent {
            Testsmobile_developmentTheme {
                MainMenu()
            }
        }
        lifecycleScope.launch {
            User.fetchUsers() // Call the fetchUsers function
        }
    }
}

private val colorScheme = ColorScheme()

private val CustomColorPalette = lightColorScheme(
    primary = colorScheme.primary,
    onPrimary = colorScheme.white,
    secondary = colorScheme.secondary,
    onSecondary = colorScheme.white,
    background = colorScheme.white,
    onBackground = Color.Black,
    surface = colorScheme.white,
    onSurface = Color.Black,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainMenu() {
    val context = LocalContext.current

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            MediumTopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(start = 30.dp)
                    ) {
                        Text(
                            "Menú Principal",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = CustomColorPalette.onPrimary,
                            fontSize = 24.sp
                        )
                    }
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = CustomColorPalette.primary
                ),
                scrollBehavior = scrollBehavior
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = {
                        val intent = Intent(context, SignUpActivity::class.java)
                        context.startActivity(intent)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CustomColorPalette.secondary,
                        contentColor = CustomColorPalette.onSecondary
                    ),
                    modifier = Modifier
                        .padding(20.dp)
                        .height(60.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        "Registrarse",
                        fontSize = 20.sp
                    )
                }
                Button(
                    onClick = {
                        val intent = Intent(context, LoginActivity::class.java)
                        context.startActivity(intent)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CustomColorPalette.secondary,
                        contentColor = CustomColorPalette.onSecondary
                    ),
                    modifier = Modifier
                        .padding(20.dp)
                        .height(60.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        "Iniciar sesión",
                        fontSize = 20.sp
                    )
                }
            }
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "App Logo",
                modifier = Modifier.size(180.dp)
            )
        }
    }
}
