package jp._5000164.chat_range_exporter.domain

import jp._5000164.chat_range_exporter.interfaces.Slack
import play.api.libs.json.JsValue

object Export {
  def execute(slack: Slack, channelId: String, message: String): Either[String, String] = {
    analyze(message) match {
      case Right((oldest, latest, true)) =>
        val messages = slack.fetchMessages(channelId, latest, oldest)
        val replies = slack.fetchReplies(channelId, (messages.head \ "thread_ts").as[String])
        Right(transform(replies))
      case Right((oldest, latest, false)) =>
        val messages = slack.fetchMessages(channelId, latest, oldest)
        Right(transform(messages.reverse))
      case Left(error) =>
        Left(error)
    }
  }

  def analyze(message: String): Either[String, (String, String, Boolean)] = {
    message.split(' ') match {
      case partList if partList.length == 2 =>
        val oldest = transformTimestamp(getTimestamp(partList(1).drop(1).dropRight(1)), oldestFlag = true)
        val latest = transformTimestamp(getTimestamp(partList(1).drop(1).dropRight(1)), oldestFlag = false)
        Right((oldest, latest, true))
      case partList if partList.length == 3 =>
        val oldest = transformTimestamp(getTimestamp(partList(1).drop(1).dropRight(1)), oldestFlag = true)
        val latest = transformTimestamp(getTimestamp(partList(2).drop(1).dropRight(1)), oldestFlag = false)
        Right((oldest, latest, false))
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
    messages.map(filter).mkString("\n")

  private def filter(message: JsValue): String =
    s"${(message \ "ts").as[String]} ${(message \ "user").as[String]} ${(message \ "text").as[String]}"
}
