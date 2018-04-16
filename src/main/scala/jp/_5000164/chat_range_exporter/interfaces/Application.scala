package jp._5000164.chat_range_exporter.interfaces

object Application extends App {
  val slack = new Slack(sys.env("SLACK_TOKEN"))
  val operator = new Operator(slack)
  slack.start(operator)
}
