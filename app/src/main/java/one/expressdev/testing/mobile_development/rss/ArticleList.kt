package one.expressdev.testing.mobile_development.rss

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import one.expressdev.testing.mobile_development.rss.ui.theme.Testsmobile_developmentTheme

class ArticleList : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        var articles by mutableStateOf<List<RssEntry>>(emptyList())
        var isLoading by mutableStateOf(true)

        lifecycleScope.launch {
            articles = loadRssFeed()
            isLoading = false
        }

        setContent {
            Testsmobile_developmentTheme {
                ArticleListScreen(articles, isLoading)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticleListScreen(articles: List<RssEntry>, isLoading: Boolean) {

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("Articles") }
                )
            }
        ) { innerPadding ->

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(articles) { article ->
                    ArticleCard(article)
                }
            }
        }
    }
}

@Composable
fun ArticleCard(article: RssEntry) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = article.title, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "By ${article.author}", style = MaterialTheme.typography.bodySmall)

        }
    }
}

@Preview(showBackground = true)
@Composable
fun ArticleListPreview() {
    Testsmobile_developmentTheme {
        ArticleListScreen(emptyList(), false)
    }
}
