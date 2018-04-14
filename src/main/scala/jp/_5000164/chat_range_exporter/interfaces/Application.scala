package jp._5000164.chat_range_exporter.interfaces

import akka.actor.ActorSystem
import slack.api.BlockingSlackApiClient
import slack.rtm.SlackRtmClient

import scala.concurrent.ExecutionContextExecutor

object Application extends App {
  implicit val system: ActorSystem = ActorSystem("slack")
  implicit val ec: ExecutionContextExecutor = system.dispatcher
  val token = sys.env("SLACK_TOKEN")
  val rtmClient = SlackRtmClient(token)
  val apiClient = BlockingSlackApiClient(token)
  val botId = rtmClient.state.self.id
  val operator = new Operator(rtmClient, botId, apiClient)
  rtmClient.onMessage(message => operator.run(message))
}
