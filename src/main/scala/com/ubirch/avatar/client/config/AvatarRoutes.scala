package com.ubirch.avatar.client.config

object AvatarRoutes {

  val host = AvatarClientConfig.host

  val urlCheck = s"$host${AvatarRouteKeys.pathCheck}"

  val urlDeepCheck = s"$host${AvatarRouteKeys.pathDeepCheck}"

  val urlDevice = s"$host${AvatarRouteKeys.pathDevice}"

  val urlDeviceClaim = s"$host${AvatarRouteKeys.pathDeviceClaim}"

  val urlDeviceStub = s"$host${AvatarRouteKeys.pathDeviceStub}"

  val urlDeviceUpdate = s"$host${AvatarRouteKeys.pathDeviceUpdate}"

  val urlDeviceUpdateBulk = s"$host${AvatarRouteKeys.pathDeviceBulk}"

  def urlDeviceWithId(deviceId: String) = s"$host${AvatarRouteKeys.pathDeviceWithId(deviceId)}"

  def urlDataTransferDates(deviceId: String) = s"$host${AvatarRouteKeys.pathDeviceDataTransferDates(deviceId)}"
}
