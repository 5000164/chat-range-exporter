package jp._5000164.chat_range_exporter.domain

import jp._5000164.chat_range_exporter.interfaces.Slack
import play.api.libs.json.JsValue

object Export {
  def execute(slack: Slack, channelId: String, message: String): Either[String, String] = {
    analyze(message) match {
      case Right((oldest, latest)) =>
        val messages = slack.fetchMessages(channelId, latest, oldest)
        Right(transform(messages))
      case Left(error) =>
        Left(error)
    }
  }

  def analyze(message: String): Either[String, (String, String)] = {
    message.split(' ') match {
      case partList if partList.length == 3 =>
        val oldest = transformTimestamp(getTimestamp(partList(1).drop(1).dropRight(1)), true)
        val latest = transformTimestamp(getTimestamp(partList(2).drop(1).dropRight(1)), false)
        Right((oldest, latest))
      case _ => Left("引数の数がおかしいです")
    }
  }

  private def getTimestamp(url: String): String =
    url.split('/').last

  private def transformTimestamp(timestamp: String, oldestFlag: Boolean): String = {
    val integerPart = timestamp.slice(1, 11)
    val fractionalPart = "%06d".format(timestamp.takeRight(6).toInt + (if (oldestFlag) -1 else 1))
    s"$integerPart.$fractionalPart"
  }

  def transform(messages: Seq[JsValue]): String =
    messages.map(filter).reverse.mkString("\n")

  private def filter(message: JsValue): String =
    s"${(message \ "ts").as[String]} ${(message \ "user").as[String]} ${(message \ "text").as[String]}"
}
