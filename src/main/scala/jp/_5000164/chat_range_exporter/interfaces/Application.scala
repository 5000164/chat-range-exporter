package jp._5000164.chat_range_exporter.interfaces

object Application extends App {
  val slack = new Slack(sys.env("SLACK_TOKEN"))
  val backlog = new Backlog(sys.env("BACKLOG_SPACE_ID"), sys.env("BACKLOG_API_KEY"), sys.env("BACKLOG_WIKI_ID"))
  val operator = new Operator(slack, backlog)
  slack.start(operator)
}
