package jp._5000164.chat_range_exporter.interfaces

import jp._5000164.chat_range_exporter.domain.Export
import slack.SlackUtil
import slack.models.Message

class Operator(val slack: Slack, val backlog: Backlog) {
  def run(message: Message): Unit = {
    val mentionedIds = SlackUtil.extractMentionedIds(message.text)
    if (mentionedIds.contains(slack.botId)) {
      Export.execute(slack.apiClient, message.channel, message.text) match {
        case Right(result) =>
          backlog.write(result)
          slack.rtmClient.sendMessage(message.channel, "成功")
        case Left(error) => slack.rtmClient.sendMessage(message.channel, error)
      }
    }
  }
}
