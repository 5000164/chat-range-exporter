package jp._5000164.chat_range_exporter.domain

import org.scalatest.FreeSpec
import play.api.libs.json.Json

class ExportSpec extends FreeSpec {
  "チャットメッセージの引数が 1 つの場合にタイムスタンプの範囲を取り出せる" in {
    val message = "<@AAAAAAAAA> <https://my.slack.com/archives/AAAAAAAAA/p0000000000000001>"
    assert(Export.analyze(message) == Right("0000000000.000000", "0000000000.000002", true))
  }

  "チャットメッセージの引数が 2 つの場合にタイムスタンプの範囲を取り出せる" in {
    val message = "<@AAAAAAAAA> <https://my.slack.com/archives/AAAAAAAAA/p0000000000000001> <https://my.slack.com/archives/AAAAAAAAA/p0000000000000002>"
    assert(Export.analyze(message) == Right("0000000000.000000", "0000000000.000003", false))
  }

  "Slack のメッセージをエクスポート用に整形する" in {
    val messages = Seq(Json.parse( """{"type":"message","user":"AAAAAAAAA","text":"text","client_msg_id":"00000000-0000-0000-0000-000000000000","ts":"0000000000.000001"}"""))
    assert(Export.transform(messages) == "0000000000.000001 AAAAAAAAA text")
  }
}
