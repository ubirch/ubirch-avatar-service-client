package com.ubirch.avatar.client.model

import java.util.UUID

import com.ubirch.util.uuid.UUIDUtil
import org.joda.time.{DateTime, DateTimeZone}

/**
  * @param mid measurementid
  * @param did tarckle device id
  * @param ts  Timestamp of measurement
  * @param te  temperature
  * @param er  error code
  */
final case class TrackleSensorMeasurement(
                                           mid: UUID = UUIDUtil.uuid,
                                           pid: Option[UUID] = None,
                                           did: String,
                                           ts: DateTime = DateTime.now(DateTimeZone.UTC),
                                           te: BigDecimal,
                                           er: Int = 0
                                         )
