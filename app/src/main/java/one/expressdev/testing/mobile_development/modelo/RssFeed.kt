package one.expressdev.testing.mobile_development.modelo
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import com.google.firebase.Firebase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database
import com.google.firebase.database.getValue
import kotlinx.coroutines.tasks.await

data class RssFeed(
    val id: String = java.util.UUID.randomUUID().toString(),
    val name: String = "", // Default value
    val url: String = ""   // Default value
) {
    companion object {
        private val database: DatabaseReference = Firebase.database.reference.child("rssFeeds")
        private val feedsList = mutableStateListOf<RssFeed>()

        // Fetch all RSS feeds from Firebase
        suspend fun fetchFeeds() {
            try {
                val snapshot = database.get().await()
                feedsList.clear()
                snapshot.children.forEach { child ->
                    val feed = child.getValue<RssFeed>()
                    feed?.let { feedsList.add(it) }
                }

                // Log the fetched feeds
                Log.d("RssFeed", "Feeds fetched successfully: ${feedsList.joinToString { it.toString() }}")
            } catch (e: Exception) {
                Log.e("RssFeed", "Error fetching feeds: ${e.message}")
            }
        }

        // Push a single feed to Firebase
        private fun pushFeed(feed: RssFeed) {
            database.child(feed.id).setValue(feed).addOnSuccessListener {
                Log.d("RssFeed", "Feed ${feed.name} added successfully!")
            }.addOnFailureListener { error ->
                Log.e("RssFeed", "Error adding feed: ${error.message}")
            }
        }

        // Add a new feed
        fun addFeed(feed: RssFeed) {
            feedsList.add(feed)
            pushFeed(feed)
        }

        // Get the list of feeds
        fun getFeeds(): List<RssFeed> = feedsList

        // Update an existing feed
        fun updateFeed(updatedFeed: RssFeed) {
            val index = feedsList.indexOfFirst { it.id == updatedFeed.id }
            if (index != -1) {
                feedsList[index] = updatedFeed
                pushFeed(updatedFeed)
            }
        }

        // Remove a feed
        fun removeFeed(feed: RssFeed) {
            feedsList.remove(feed)
            deleteFeedFromFirebase(feed)
        }

        // Delete a feed from Firebase
        private fun deleteFeedFromFirebase(feed: RssFeed) {
            database.child(feed.id).removeValue().addOnSuccessListener {
                Log.d("RssFeed", "Feed ${feed.name} deleted successfully!")
            }.addOnFailureListener { error ->
                Log.e("RssFeed", "Error deleting feed: ${error.message}")
            }
        }
    }
}
