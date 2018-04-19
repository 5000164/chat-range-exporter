package jp._5000164.chat_range_exporter.domain

import org.scalatest.FreeSpec
import play.api.libs.json.Json

class ExportSpec extends FreeSpec {
  "メッセージからリンクを取り出す" in {
    val message = "<@AAAAAAAAA> <https://my.slack.com/archives/AAAAAAAAA/p0000000000000001> <https://my.slack.com/archives/AAAAAAAAA/p0000000000000002>"
    assert(Export.analyze(message) == Right("0000000000.000000", "0000000000.000003"))
  }

  "Slack のメッセージをエクスポート用に整形する" in {
    val messages = Seq(Json.parse( """{"type":"message","user":"AAAAAAAAA","text":"text","client_msg_id":"00000000-0000-0000-0000-000000000000","ts":"0000000000.000001"}"""))
    assert(Export.transform(messages) == "0000000000.000001 AAAAAAAAA text")
  }
}
