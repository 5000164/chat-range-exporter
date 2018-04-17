package jp._5000164.chat_range_exporter.interfaces

import akka.actor.ActorSystem
import slack.api.BlockingSlackApiClient
import slack.rtm.SlackRtmClient

import scala.concurrent.ExecutionContextExecutor

class Slack(val token: String) {
  implicit val system: ActorSystem = ActorSystem("slack")
  implicit val ec: ExecutionContextExecutor = system.dispatcher
  val apiClient = BlockingSlackApiClient(token)
  val rtmClient = SlackRtmClient(token)
  val botId: String = rtmClient.state.self.id

  def start(operator: Operator): Unit = {
    rtmClient.onMessage(message => operator.run(message))
  }
}