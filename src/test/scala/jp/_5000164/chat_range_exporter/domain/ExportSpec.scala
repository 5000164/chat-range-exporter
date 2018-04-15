package jp._5000164.chat_range_exporter.domain

import org.scalatest.FreeSpec

class ExportSpec extends FreeSpec {
  "メッセージからリンクを取り出す" in {
    val message = "<@AAAAAAAAA> https://my.slack.com/archives/AAAAAAAAA/p0000000000000000 https://my.slack.com/archives/AAAAAAAAA/p0000000000000001"
    assert(Export.analyze(message) == Right("0000000000.000000", "0000000000.000001"))
  }
}
