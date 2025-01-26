package one.expressdev.testing.mobile_development.rss

import android.os.Bundle
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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.prof18.rssparser.model.RssItem
import one.expressdev.testing.mobile_development.rss.ui.theme.Testsmobile_developmentTheme
import  one.expressdev.testing.mobile_development.Utils

import  one.expressdev.testing.mobile_development.modelo.RssFeed
class ArticleList : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Testsmobile_developmentTheme {
                ArticleListScreen()
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
        mutableStateOf(ArticleScreenState(isLoading = true, articles = emptyList(), selectedArticle = null, baseUrl = ""))
    }

    LaunchedEffect(Unit) {
        val parserRss = ParserRss()
        val allArticles = mutableListOf<RssItem>()

        RssFeed.getAllFeeds().forEach { feed ->
            parserRss.getRssChannel(feed.url,
                onSuccess = { rssChannel ->
                    allArticles.addAll(rssChannel.items)
                    articleState = articleState.copy(
                        isLoading = false,
                        articles = allArticles,
                        baseUrl = Utils().getBaseUrl(rssChannel.link.toString())
                    )
                },
                onError = {
                    articleState = articleState.copy(isLoading = false)
                    println(it)
                }
            )
        }
    }

    Scaffold(

    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding)) {
            when {
                articleState.isLoading ->
                    CircularProgressIndicator(Modifier.align(Alignment.Center))

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
        items(articles) { article ->
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
            article.title?.let { Text(it, style = MaterialTheme.typography.titleMedium) }
            Spacer(Modifier.height(4.dp))
            Text(article.author ?: "", style = MaterialTheme.typography.bodyMedium)
        }
        Icon(Icons.Default.ArrowForward, contentDescription = "Open article")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticleReader(article: RssItem, baseUrl: String , onBack: () -> Unit) {
    println("Look Mum!!"+ baseUrl)
    val isDarkTheme = isSystemInDarkTheme()

    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = {
                    Text(
                        article.title ?: "Article",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
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