package one.expressdev.testing.mobile_development.modelo

import androidx.compose.runtime.mutableStateListOf

data class RssFeed(
    val id: String = java.util.UUID.randomUUID().toString(),
    val name: String,
    val url: String

) {
    companion object {


        private val _feeds = mutableStateListOf<RssFeed>(RssFeed("9802893012jk09jfdw2",name= "KX Studio","https://kx.studio/News/?action=feed"),RssFeed("dsadlasjkfladfas","The intercept","https://theintercept.com/feed/?lang=en"),
            RssFeed("kdlsalkdlksadlsadldlaksdlkasjdpo","Prensa Austral","https://laprensaaustral.cl/rss")
        )
        val feeds: List<RssFeed> get() = _feeds

        fun addFeed(feed: RssFeed) {
            _feeds.add(feed)
            println("Adding: " + feed)
        }

        fun removeFeed(feed: RssFeed) {
            _feeds.remove(feed)
        }

        fun getAllFeeds(): List<RssFeed> = _feeds.toList()
    }
}
