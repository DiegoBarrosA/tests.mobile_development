package one.expressdev.testing.mobile_development.rss

import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL
data class RssEntry(
    var title: String,
    var id: String,
    var updated: String,
    var published: String,
    var author: String,
    var content: String
)
class RssFeedRetriever {
    suspend fun fetchXmlFromUrl(urlString: String): String {
        return withContext(Dispatchers.IO) {
            try {
                URL(urlString).readText()
            } catch (e: Exception) {
                throw RuntimeException("Error fetching XML: ${e.message}")
            }
        }
    }
}
class RssFeedParser {
    fun parseRssFeed(xmlString: String): List<RssEntry> {
        val entries = mutableListOf<RssEntry>()
        val factory = XmlPullParserFactory.newInstance()
        val parser = factory.newPullParser()
        parser.setInput(xmlString.reader())

        var currentEntry: RssEntry? = null
        var currentTag: String? = null

        while (parser.eventType != XmlPullParser.END_DOCUMENT) {
            when (parser.eventType) {
                XmlPullParser.START_TAG -> {
                    currentTag = parser.name
                    when (currentTag) {
                        "entry" -> currentEntry = RssEntry("", "", "", "", "", "")
                    }
                }
                XmlPullParser.TEXT -> {
                    when (currentTag) {
                        "title" -> currentEntry?.title = parser.text
                        "id" -> currentEntry?.id = parser.text
                        "updated" -> currentEntry?.updated = parser.text
                        "published" -> currentEntry?.published = parser.text
                        "name" -> currentEntry?.author = parser.text
                        "content" -> currentEntry?.content = parser.text
                    }
                }
                XmlPullParser.END_TAG -> {
                    if (parser.name == "entry" && currentEntry != null) {
                        entries.add(currentEntry)
                        currentEntry = null
                    }
                    currentTag = null
                }
            }
            parser.next()
        }

        return entries
    }
}


fun main() {
    val xmlString = ""
    val parser = RssFeedParser()
    val entries = parser.parseRssFeed(xmlString)
    entries.forEach { entry ->
        println("Title: ${entry.title}")
        println("Author: ${entry.author}")
        println("Published: ${entry.published}")
        println("---")
    }
}
suspend fun loadRssFeed() {
    val retriever = RssFeedRetriever()
    val xmlString = retriever.fetchXmlFromUrl("https://kx.studio/News/?action=feed")
    val parser = RssFeedParser()
    val entries = parser.parseRssFeed(xmlString)
    entries.forEach { entry ->
        println("Title: ${entry.title}")
        println("Author: ${entry.author}")
        println("Published: ${entry.published}")
        println("Content: ${entry.content}")

        println("---")
    }
}