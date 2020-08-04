package com.ubirch.avatar.client.config

object AvatarRouteKeys {

  val apiPrefix = "api"
  val currentVersion = "v1"
  val serviceName = "avatarService"
  val check = "check"
  val deepCheck = "deepCheck"
  val device = "device"
  val stub = "stub"
  val claim = "claim"
  val update = "update"
  val bulk = "bulk"
  val transferDates = "transferDates"
  val data = "data"
  val pathPrefix = s"/$apiPrefix/$serviceName/$currentVersion"
  val pathCheck = s"$pathPrefix/$check"
  val pathDeepCheck = s"$pathPrefix/$deepCheck"
  val pathDevice = s"$pathPrefix/$device"
  val pathDeviceBulk = s"$pathDevice/$update/$bulk"

  def pathDeviceWithId(id: String): String = s"$pathDevice/$id"

  val pathDeviceStub = s"$pathDevice/$stub"
  val pathDeviceClaim = s"$pathDevice/$claim"

  def pathDeviceDataTransferDates(deviceId: String): String = s"$pathDevice/$data/$transferDates/$deviceId"

  val pathDeviceUpdate: String = s"$pathDevice/$update"


}
