package one.expressdev.testing.mobile_development.rss

import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
data class RssEntry(
    var title: String,
    var id: String,
    var updated: String,
    var published: String,
    var author: String,
    var content: String,
    var baseUrl: String
)


data class FeedItem(
    val title: String,
    val link: String,
    val description: String,
    val pubDate: String

)

fun parseRssFeed(feedUrl: String): List<FeedItem> {
    val items = mutableListOf<FeedItem>()

    try {
        val doc: Document = Jsoup.connect(feedUrl).get()
        val entries: Elements = doc.select("item")

        for (entry in entries) {
            val title: String = entry.select("title").text()
            val link: String = entry.select("link").text()
            val description: String = entry.select("description").text()
            val pubDate: String = entry.select("pubDate").text()

            items.add(FeedItem(title, link, description, pubDate))
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return items
}


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
    private var baseUrl: String? = null

    fun parseRssFeed(xmlString: String): List<RssEntry> {
        val entries = mutableListOf<RssEntry>()
        val factory = XmlPullParserFactory.newInstance()
        val parser = factory.newPullParser()
        parser.setInput(xmlString.reader())
        println("Here it goes!  " + extractBaseUrlAndFeedUrl(parser))
        extractBaseUrlAndFeedUrl(parser)

        var currentEntry: RssEntry? = null
        var currentTag: String? = null

        while (parser.eventType != XmlPullParser.END_DOCUMENT) {
            when (parser.eventType) {
                XmlPullParser.START_TAG -> {
                    currentTag = parser.name
                    if (currentTag == "entry" || currentTag == "item") {
                        currentEntry = RssEntry("", "", "", "", "", "", baseUrl ?: "")
                    }
                }
                XmlPullParser.TEXT -> {
                    currentEntry?.let {
                        when (currentTag) {
                            "title" -> it.title = parser.text
                            "id" -> it.id = parser.text
                            "updated" -> it.updated = parseDate(parser.text)
                            "published" -> it.published = parseDate(parser.text)
                            "name" -> it.author = parser.text
                            "content" -> it.content = parser.text
                            "content:encoded" -> it.content = parser.text
                        }
                    }
                }
                XmlPullParser.END_TAG -> {
                    if ((parser.name == "entry" || parser.name == "item") && currentEntry != null) {
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

    private fun extractBaseUrlAndFeedUrl(parser: XmlPullParser) {
        var inFeed = false
        var baseUrl: String? = null
        var feedUrl: String? = null

        while (parser.eventType != XmlPullParser.END_DOCUMENT) {
            if (parser.eventType == XmlPullParser.START_TAG) {
                when (parser.name) {
                    "feed" -> inFeed = true
                    "link" -> {
                        if (inFeed) {
                            feedUrl = parser.getAttributeValue(null, "href")
                        } else {
                            baseUrl = parser.getAttributeValue(null, "href") ?: parser.nextText()
                        }
                    }
                    "atom:link" -> {
                        if (inFeed) {
                            feedUrl = parser.getAttributeValue(null, "href")
                        } else {
                            baseUrl = parser.getAttributeValue(null, "href")
                        }
                    }
                }
            }
            parser.next()
        }

        // Use the extracted URLs as needed
        println("Base URL: $baseUrl")
        println("Feed URL: $feedUrl")
    }

    private fun parseDate(dateString: String): String {
        // Implement date parsing logic here
        return dateString
    }
}

suspend fun loadRssFeed(urlString: String): List<RssEntry> {

    val feedUrl = "https://theintercept.com/feed/?lang=en"
    val feedItems = parseRssFeed(feedUrl)

    for (item in feedItems) {
        println("Title: ${item.title}")
        println("Link: ${item.link}")
        println("Description: ${item.description}")
        println("Publication Date: ${item.pubDate}")
        println()
    }

    val retriever = RssFeedRetriever()
    val xmlString = retriever.fetchXmlFromUrl(urlString)
//    val xmlString = retriever.fetchXmlFromUrl("https://theintercept.com/feed/?lang=en")

    val parser = RssFeedParser()
    val entries = parser.parseRssFeed(xmlString)

    return entries
}

