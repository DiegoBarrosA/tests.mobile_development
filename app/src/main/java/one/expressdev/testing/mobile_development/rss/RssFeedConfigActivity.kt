package one.expressdev.testing.mobile_development.rss

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import one.expressdev.testing.mobile_development.rss.ui.theme.Testsmobile_developmentTheme

import  one.expressdev.testing.mobile_development.modelo.RssFeed

class RssFeedConfigActivity : ComponentActivity() {
    private val rssFeedRepository = RssFeed.Companion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Testsmobile_developmentTheme {
                RssFeedConfigScreen(
                    repository = rssFeedRepository,
                    onAddFeed = { feed -> rssFeedRepository.addFeed(feed) },
                    onRemoveFeed = { feed -> rssFeedRepository.removeFeed(feed) }
                )
            }
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RssFeedConfigScreen(
    repository: RssFeed.Companion,
    onAddFeed: (RssFeed) -> Unit,
    onRemoveFeed: (RssFeed) -> Unit
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var newFeedName by remember { mutableStateOf("") }
    var newFeedUrl by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            SmallTopAppBar(title = { Text("RSS Feed Configuration") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add Feed")
            }
        }
    ) { padding ->
        Column(Modifier.padding(padding)) {
            LazyColumn {
                items(repository.feeds) { feed ->
                    RssFeedItem(
                        feed = feed,
                        onDelete = { onRemoveFeed(feed) }
                    )
                }
            }
        }

        if (showAddDialog) {
            AlertDialog(
                onDismissRequest = { showAddDialog = false },
                title = { Text("Add New RSS Feed") },
                text = {
                    Column {
                        TextField(
                            value = newFeedName,
                            onValueChange = { newFeedName = it },
                            label = { Text("Feed Name") }
                        )
                        Spacer(Modifier.height(8.dp))
                        TextField(
                            value = newFeedUrl,
                            onValueChange = { newFeedUrl = it },
                            label = { Text("Feed URL") }
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
                        }
                    ) {
                        Text("Add")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showAddDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
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
            Text(feed.name, style = MaterialTheme.typography.titleMedium)
            Text(feed.url, style = MaterialTheme.typography.bodySmall)
        }
        IconButton(onClick = onDelete) {
            Icon(Icons.Default.Delete, contentDescription = "Delete Feed")
        }
    }
}