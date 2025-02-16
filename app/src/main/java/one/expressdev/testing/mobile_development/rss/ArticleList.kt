package one.expressdev.testing.mobile_development.rss

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.lifecycleScope
import com.prof18.rssparser.model.RssItem
import kotlinx.coroutines.launch
import one.expressdev.testing.mobile_development.Utils
import one.expressdev.testing.mobile_development.modelo.RssFeed
import one.expressdev.testing.mobile_development.rss.ui.theme.Testsmobile_developmentTheme

class ArticleList : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Testsmobile_developmentTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFFFFFFFF)
                ) {
                    ArticleListScreen()
                }
            }
        }
    }
}

data class ArticleScreenState(
    val isLoading: Boolean,
    val articles: List<RssItem>?,
    val selectedArticle: RssItem?,
    val baseUrl: String
)

@Composable
fun ArticleListScreen() {
    var articleState by remember {
        mutableStateOf(
            ArticleScreenState(
                isLoading = true,
                articles = emptyList(),
                selectedArticle = null,
                baseUrl = ""
            )
        )
    }

    val context = LocalContext.current
    LaunchedEffect(Unit) {
        val parserRss = ParserRss()
        val allArticles = mutableListOf<RssItem>()
        val feedsFetch = RssFeed.fetchFeeds()
        val feeds = RssFeed.getFeeds()
        val feedsCount = feeds.size
        var feedsProcessed = 0

        if (feedsCount == 0) {
            Log.d("ArticleListScreen", "No feeds to process.")
            articleState = articleState.copy(isLoading = false)
        }

        feeds.forEach { feed ->
            Log.d("ArticleListScreen", "Fetching articles from: ${feed.url}")
            parserRss.getRssChannel(feed.url,
                onSuccess = { rssChannel ->
                    Log.d("ArticleListScreen", "Successfully fetched articles from: ${feed.url}")
                    allArticles.addAll(rssChannel.items)
                    feedsProcessed++
                    if (feedsProcessed == feedsCount) {
                        articleState = articleState.copy(
                            isLoading = false,
                            articles = allArticles,
                            baseUrl = Utils().getBaseUrl(rssChannel.link.toString())
                        )
                        Log.d("ArticleListScreen", "All feeds processed. Total articles: ${allArticles.size}")
                    }
                },
                onError = { error ->
                    Log.e("ArticleListScreen", "Error fetching articles from: ${feed.url} - ${error.message}")
                    feedsProcessed++
                    if (feedsProcessed == feedsCount) {
                        articleState = articleState.copy(isLoading = false)
                    }
                }
            )
        }
    }

    Scaffold(
        containerColor = Color(0xFFFFFFFF),
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    val intent = Intent(context, RssFeedConfigActivity::class.java)
                    context.startActivity(intent)
                },
                containerColor = Color(0xFF175770)
            ) {
                Icon(
                    Icons.Filled.Settings,
                    contentDescription = "Configuración",
                    tint = Color(0xFFFFFFFF)
                )
            }
        }
    ) { padding ->
        Box(Modifier
            .fillMaxSize()
            .padding(padding)) {
            when {
                articleState.isLoading ->
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = Color(0xFF175770)
                    )

                articleState.articles?.isNotEmpty() == true ->
                    articleState.articles?.let {
                        ArticleList(
                            articles = it,
                            onArticleSelect = {
                                articleState = articleState.copy(selectedArticle = it)
                            }
                        )
                    }
            }

            articleState.selectedArticle?.let {
                ArticleReader(it, articleState.baseUrl) {
                    articleState = articleState.copy(selectedArticle = null)
                }
            }
        }
    }
}

@Composable
fun ArticleList(
    articles: List<RssItem>,
    onArticleSelect: (RssItem) -> Unit
) {
    LazyColumn(Modifier.fillMaxSize()) {
        items(articles.sortedByDescending { it.pubDate }) { article ->
            ArticleListItem(article, onArticleSelect)
        }
    }
}

@Composable
fun ArticleListItem(article: RssItem, onClick: (RssItem) -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .clickable { onClick(article) }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(Modifier.weight(1f)) {
            article.title?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color(0xFF175770)
                )
            }
            Spacer(Modifier.height(4.dp))
            Text(
                text = article.author ?: "",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF254691)
            )
        }
        Icon(
            Icons.Default.ArrowForward,
            contentDescription = "Abrir artículo",
            tint = Color(0xFF43409B)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticleReader(article: RssItem, baseUrl: String, onBack: () -> Unit) {
    val isDarkTheme = isSystemInDarkTheme()

    Scaffold(
        containerColor = Color(0xFFFFFFFF),
        topBar = {
            SmallTopAppBar(
                title = {
                    Text(
                        text = article.title ?: "Artículo",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = Color(0xFF175770)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Atrás",
                            tint = Color(0xFF43409B)
                        )
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = Color(0xFFFFFFFF),
                    titleContentColor = Color(0xFF175770),
                    navigationIconContentColor = Color(0xFF43409B)
                )
            )
        }
    ) { padding ->
        AndroidView(
            factory = { context ->
                WebView(context).apply {
                    webViewClient = WebViewClient()
                    settings.apply {
                        javaScriptEnabled = true
                        loadsImagesAutomatically = true
                    }
                }
            },
            update = { webView ->
                val (backgroundColor, textColor) = if (isDarkTheme)
                    "#121212" to "#FFFFFF"
                else
                    "#FFFFFF" to "#000000"

                val html = """
                   <html>
                       <head>
                           <style>
                               body {
                                   font-family: Arial, sans-serif;
                                   background-color: $backgroundColor;
                                   color: $textColor;
                                   margin: 0;
                                   padding: 16px;
                                   font-size: 1.4em;
                                   line-height: 1.6;
                               }
                               h1 { margin: 0 0 16px; }
                               img { max-width: 100%; height: auto; }
                           </style>
                       </head>
                       <body>
                           <h1>${article.title}</h1>
                           ${article.content}
                       </body>
                   </html>
               """.trimIndent()

                webView.loadDataWithBaseURL(baseUrl, html, "text/html", "UTF-8", null)
            },
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        )
    }
}