package com.ubich.avatar.client.model

import org.json4s.JValue

case class DeviceType(
                       key: String,
                       name: DeviceTypeName,
                       icon: String,
                       displayKeys: Option[Array[String]] = Some(Array.empty),
                       transformerQueue: Option[String] = None,
                       defaults: DeviceTypeDefaults
                     ) {

  override def hashCode(): Int = key.hashCode

  override def toString: String = s"deviceType.key=$key"

  override def equals(o: scala.Any): Boolean = {

    o match {

      case deviceType: DeviceType =>

        if (deviceType.key == this.key)
          true
        else
          false

      case _ => false

    }
  }

}

case class DeviceTypeName(
                           de: String,
                           en: String
                         )

case class DeviceTypeDefaults(
                               properties: JValue,
                               config: JValue,
                               tags: Set[String]
                             )
