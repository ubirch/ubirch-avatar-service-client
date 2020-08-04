package com.ubirch.avatar.client.config

import com.ubirch.util.config.ConfigBase

/**
  * author: cvandrei
  * since: 2018-10-17
  */
object AvatarClientConfig extends ConfigBase {

  def host = config.getString(AvatarClientConfigKeys.HOST)

}
