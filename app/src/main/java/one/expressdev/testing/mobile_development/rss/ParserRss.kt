package one.expressdev.testing.mobile_development.rss

import com.prof18.rssparser.RssParser
import com.prof18.rssparser.model.RssChannel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ParserRss {
    private val rssParser: RssParser = RssParser()

    fun getRssChannel(url: String, onSuccess: (RssChannel) -> Unit, onError: (Throwable) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val rssChannel = withContext(Dispatchers.IO) {
                    rssParser.getRssChannel(url)
                }
                withContext(Dispatchers.Main) {
                    onSuccess(rssChannel)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    onError(e)
                }
            }
        }
    }
}
