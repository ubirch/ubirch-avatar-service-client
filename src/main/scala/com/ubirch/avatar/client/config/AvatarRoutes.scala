package com.ubirch.avatar.client.config

object AvatarRoutes {

  val host: String = AvatarClientConfig.host

  val urlCheck: String = s"$host${AvatarRouteKeys.pathCheck}"

  val urlDeepCheck: String = s"$host${AvatarRouteKeys.pathDeepCheck}"

  val urlDevice: String = s"$host${AvatarRouteKeys.pathDevice}"

  val urlDeviceClaim: String = s"$host${AvatarRouteKeys.pathDeviceClaim}"

  val urlDeviceStub: String = s"$host${AvatarRouteKeys.pathDeviceStub}"

  val urlDeviceUpdate: String = s"$host${AvatarRouteKeys.pathDeviceUpdate}"

  val urlDeviceUpdateBulk: String = s"$host${AvatarRouteKeys.pathDeviceBulk}"

  def urlDeviceWithId(deviceId: String): String = s"$host${AvatarRouteKeys.pathDeviceWithId(deviceId)}"

  def urlDataTransferDates(deviceId: String): String = s"$host${AvatarRouteKeys.pathDeviceDataTransferDates(deviceId)}"
}
