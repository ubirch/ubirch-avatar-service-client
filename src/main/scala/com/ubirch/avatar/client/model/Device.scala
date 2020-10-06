package com.ubirch.avatar.client.model

import java.util.UUID

import org.joda.time.{DateTime, DateTimeZone}
import org.json4s.JValue


case class Device(deviceId: String,
                  owners: Set[UUID] = Set.empty,
                  groups: Set[UUID] = Set.empty,
                  deviceTypeKey: String = "unknownDeviceType",
                  deviceName: String = "unnamedDevice",
                  hwDeviceId: String = "unknownHwDeviceId",
                  hashedHwDeviceId: String = "unknownHwDeviceId",
                  tags: Set[String] = Set(),
                  deviceConfig: Option[JValue] = None,
                  deviceProperties: Option[JValue] = None,
                  subQueues: Option[Set[String]] = None,
                  pubQueues: Option[Set[String]] = Some(Set()),
                  pubRawQueues: Option[Set[String]] = Some(Set()),
                  //                  pubQueues: Option[Set[String]] = Some(Set(Config.awsSqsQueueTransformerOut)),
                  //                  pubRawQueues: Option[Set[String]] = Some(Set(Config.awsSqsQueueTransformer)),
                  avatarLastUpdated: Option[DateTime] = None,
                  deviceLastUpdated: Option[DateTime] = None,
                  updated: Option[DateTime] = None,
                  created: DateTime = DateTime.now(DateTimeZone.UTC)
                 ) {

  override def equals(obj: scala.Any): Boolean = {
    obj match {
      case dev: Device =>
        if (dev.deviceId == this.deviceId)
          true
        else
          false
      case _ => false
    }
  }

  override def hashCode(): Int = this.deviceId.hashCode
}
