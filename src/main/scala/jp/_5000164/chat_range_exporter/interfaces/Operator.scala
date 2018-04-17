package jp._5000164.chat_range_exporter.interfaces

import jp._5000164.chat_range_exporter.domain.Export
import slack.SlackUtil
import slack.models.Message

class Operator(val slack: Slack, val backlog: Backlog) {
  def run(message: Message): Unit = {
    val mentionedIds = SlackUtil.extractMentionedIds(message.text)
    if (mentionedIds.contains(slack.botId)) {
      Export.execute(slack, message.channel, message.text) match {
        case Right(result) =>
          backlog.write(result)
          slack.post(message.channel, text = "成功")
        case Left(error) =>
          slack.post(message.channel, text = error)
      }
    }
  }
}
