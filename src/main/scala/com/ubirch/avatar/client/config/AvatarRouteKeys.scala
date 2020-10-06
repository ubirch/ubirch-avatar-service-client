package com.ubirch.avatar.client.config

object AvatarRouteKeys {

  val apiPrefix: String = "api"
  val currentVersion: String = "v1"
  val serviceName: String = "avatarService"
  val check: String = "check"
  val deepCheck: String = "deepCheck"
  val device: String = "device"
  val stub: String = "stub"
  val claim: String = "claim"
  val update: String = "update"
  val bulk: String = "bulk"
  val transferDates: String = "transferDates"
  val data: String = "data"
  val pathPrefix: String = s"/$apiPrefix/$serviceName/$currentVersion"
  val pathCheck: String = s"$pathPrefix/$check"
  val pathDeepCheck: String = s"$pathPrefix/$deepCheck"
  val pathDevice: String = s"$pathPrefix/$device"
  val pathDeviceBulk: String = s"$pathDevice/$update/$bulk"

  def pathDeviceWithId(id: String): String = s"$pathDevice/$id"

  val pathDeviceStub: String = s"$pathDevice/$stub"
  val pathDeviceClaim: String = s"$pathDevice/$claim"

  def pathDeviceDataTransferDates(deviceId: String): String = s"$pathDevice/$data/$transferDates/$deviceId"

  val pathDeviceUpdate: String = s"$pathDevice/$update"

}
