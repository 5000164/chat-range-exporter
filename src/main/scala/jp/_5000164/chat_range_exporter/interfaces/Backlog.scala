package jp._5000164.chat_range_exporter.interfaces

import com.nulabinc.backlog4j.api.option.UpdateWikiParams
import com.nulabinc.backlog4j.conf.{BacklogConfigure, BacklogJpConfigure}
import com.nulabinc.backlog4j.{BacklogClient, BacklogClientFactory}

class Backlog(val spaceId: String, val apiKey: String, val wikiId: String) {
  val configure: BacklogConfigure = new BacklogJpConfigure(spaceId).apiKey(apiKey)
  val client: BacklogClient = new BacklogClientFactory(configure).newClient()

  def write(result: String): Unit = {
    client.updateWiki(new UpdateWikiParams(wikiId).content(result))
  }
}
