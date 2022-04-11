package com.ubirch.avatar.client.model

import org.joda.time.DateTime

import java.util.UUID

case class DeviceClaim(hwDeviceId: String, userId: UUID)

case class DeviceUserClaimRequest(hwDeviceId: String,
                                  externalId: String,
                                  userId: UUID)

case class DeviceUserClaim(hwDeviceId: String,
                           deviceId: String,
                           userId: UUID,
                           productionDate: DateTime)
