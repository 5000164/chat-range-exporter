package jp._5000164.chat_range_exporter.interfaces

import jp._5000164.chat_range_exporter.domain.Export
import slack.SlackUtil
import slack.api.BlockingSlackApiClient
import slack.models.Message
import slack.rtm.SlackRtmClient

class Operator(val rtmClient: SlackRtmClient, val botId: String, val apiClient: BlockingSlackApiClient) {
  def run(message: Message): Unit = {
    val mentionedIds = SlackUtil.extractMentionedIds(message.text)
    if (mentionedIds.contains(botId)) {
      Export.execute(apiClient, message.channel, message.text) match {
        case Right(result) => rtmClient.sendMessage(message.channel, result)
        case Left(error) => rtmClient.sendMessage(message.channel, error)
      }
    }
  }
}
