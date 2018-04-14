package jp._5000164.chat_range_exporter.interfaces

import akka.actor.ActorSystem
import slack.rtm.SlackRtmClient

import scala.concurrent.ExecutionContextExecutor

object Application extends App {
  implicit val system: ActorSystem = ActorSystem("slack")
  implicit val ec: ExecutionContextExecutor = system.dispatcher
  val token = sys.env("SLACK_TOKEN")
  val client = SlackRtmClient(token)
  val botId = client.state.self.id
  client.onMessage(message => println(message))
}
