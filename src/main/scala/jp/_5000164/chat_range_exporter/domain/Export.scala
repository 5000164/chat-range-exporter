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
      case Left(_) => Left("形式が正しくありません")
    }
  }

  def analyze(message: String): Either[Unit, (String, String)] = {
    Right("1523716078.000064", "1523716133.000114")
  }
}
