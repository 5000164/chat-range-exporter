package jp._5000164.chat_range_exporter.domain

import akka.actor.ActorSystem
import slack.api.BlockingSlackApiClient

import scala.concurrent.ExecutionContextExecutor

object Export {
  def execute(client: BlockingSlackApiClient, channelId: String, message: String): Either[String, String] = {
    analyze(message) match {
      case Right((oldest, latest)) =>
        implicit val system: ActorSystem = ActorSystem("slack")
        implicit val ec: ExecutionContextExecutor = system.dispatcher
        val historyChunk = client.getChannelHistory(channelId, Some(latest), Some(oldest))
        Right(historyChunk.messages.mkString("\n"))
      case Left(error) => Left(error)
    }
  }

  def analyze(message: String): Either[String, (String, String)] = {
    message.split(' ') match {
      case partList if partList.length == 3 =>
        val oldest = transformTimestamp(getTimestamp(partList(1)))
        val latest = transformTimestamp(getTimestamp(partList(2)))
        Right(oldest, latest)
      case _ => Left("引数の数がおかしいです")
    }
  }

  private def getTimestamp(url: String): String =
    url.split('/').last

  private def transformTimestamp(timestamp: String): String =
    timestamp.slice(1, 11) + "." + timestamp.takeRight(6)
}
