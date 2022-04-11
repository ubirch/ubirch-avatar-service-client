package com.ubirch.avatar.client

import akka.http.scaladsl.HttpExt
import akka.http.scaladsl.model._
import akka.stream.Materializer
import akka.util.ByteString
import com.typesafe.scalalogging.StrictLogging
import com.ubirch.avatar.client.config.AvatarRoutes
import com.ubirch.avatar.client.model._
import com.ubirch.util.http.auth.AuthUtil
import com.ubirch.util.json.{Json4sUtil, MyJsonProtocol}
import com.ubirch.util.model.JsonErrorResponse
import org.json4s.native.Serialization.read

import java.util.UUID
import scala.concurrent.{ExecutionContextExecutor, Future}

object AvatarServiceClient extends MyJsonProtocol
  with StrictLogging {

  val statusOkCodes: Set[StatusCode] = Set(StatusCodes.OK, StatusCodes.Accepted)


  /**
    * Update a device by POSTing raw device data.
    *
    * @param deviceDataRaw raw data to POST
    * @return http response
    */
  def deviceUpdatePOST(deviceDataRaw: DeviceDataRaw)
                      (implicit httpClient: HttpExt, materializer: Materializer): Future[Either[JsonErrorResponse, DeviceStateUpdate]] = {

    implicit val ec: ExecutionContextExecutor = materializer.executionContext
    Json4sUtil.any2String(deviceDataRaw) match {

      case Some(deviceDataRawString) =>

        val url = AvatarRoutes.urlDeviceUpdate
        val req = HttpRequest(
          method = HttpMethods.POST,
          uri = url,
          entity = HttpEntity.Strict(ContentTypes.`application/json`, data = ByteString(deviceDataRawString))
        )
        httpClient.singleRequest(req) flatMap {

          case HttpResponse(status, _, entity, _) if statusOkCodes.contains(status) =>

            entity.dataBytes.runFold(ByteString(""))(_ ++ _) map { body =>
              Right(read[DeviceStateUpdate](body.utf8String))
            }

          case res@HttpResponse(code, _, entity, _) =>

            logger.error(s"deviceUpdatePOST() call to avatar-service failed: url=$url code=$code, status=${res.status}")
            entity.dataBytes.runFold(ByteString(""))(_ ++ _) map { body =>
              Left(read[JsonErrorResponse](body.utf8String))
            }

        }

      case None =>

        logger.error(s"failed to to convert input to JSON: deviceDataRaw=$deviceDataRaw")
        Future(Left(JsonErrorResponse(errorType = "RestClientError", errorMessage = "error before sending the request: failed to convert input to JSON")))

    }

  }


  /**
    * @param device       device to create
    * @param oidcToken    OpenID Connect token to use for authorization
    * @param ubirchToken  ubirch token to use for authorization (ignored if oidcToken is set)
    * @param httpClient   http connection
    * @param materializer Akka materializer required by http connection
    */
  def devicePOST(
                  device: Device,
                  oidcToken: Option[String] = None,
                  ubirchToken: Option[String] = None
                )
                (implicit httpClient: HttpExt, materializer: Materializer): Future[Either[JsonErrorResponse, Device]] = {

    implicit val ec: ExecutionContextExecutor = materializer.executionContext
    if (oidcToken.isEmpty && ubirchToken.isEmpty) {

      logger.error(s"either an OpenID Connect or ubirch token is needed to create a device: device=$device")
      Future(Left(JsonErrorResponse(errorType = "RestClientError", errorMessage = "error before sending the request: either an OpenID Connect or ubirch token")))

    } else {

      Json4sUtil.any2String(device) match {

        case Some(deviceString) =>

          val url = AvatarRoutes.urlDevice
          val req = HttpRequest(
            method = HttpMethods.POST,
            uri = url,
            headers = AuthUtil.authHeaders(oidcToken = oidcToken, ubirchToken = ubirchToken),
            entity = HttpEntity.Strict(ContentTypes.`application/json`, data = ByteString(deviceString))
          )
          httpClient.singleRequest(req) flatMap {

            case HttpResponse(status, _, entity, _) if statusOkCodes.contains(status) =>

              entity.dataBytes.runFold(ByteString(""))(_ ++ _) map { body =>
                Right(read[Device](body.utf8String))
              }

            case HttpResponse(StatusCodes.Forbidden, _, _, _) =>

              Future(Left(JsonErrorResponse(errorType = "InvalidToken", errorMessage = "login token is not valid")))

            case res@HttpResponse(code, _, entity, _) =>

              logger.error(s"devicePOST() call to avatar-service failed: url=$url code=$code, status=${res.status}")
              entity.dataBytes.runFold(ByteString(""))(_ ++ _) map { body =>
                Left(read[JsonErrorResponse](body.utf8String))
              }

          }

        case None =>

          logger.error(s"failed to to convert input to JSON: device=$device")
          Future(Left(JsonErrorResponse(errorType = "RestClientError", errorMessage = "error before sending the request: failed to convert input to JSON")))

      }

    }

  }

  /**
    * @param hwDeviceId   device to claim
    * @param ubirchToken  ubirch token to use for authorization (ignored if oidcToken is set)
    * @param httpClient   http connection
    * @param materializer Akka materializer required by http connection
    */
  def claimDevicePUT(userId: UUID,
                     hwDeviceId: String,
                     ubirchToken: Option[String] = None)
                    (implicit httpClient: HttpExt, materializer: Materializer): Future[Either[JsonErrorResponse, DeviceUserClaim]] = {

    implicit val ec: ExecutionContextExecutor = materializer.executionContext
    if (ubirchToken.isEmpty) {

      logger.error("either an OpenID Connect or ubirch token is needed to claim a device")
      Future(Left(JsonErrorResponse(errorType = "RestClientError", errorMessage = "error before sending the request: either an OpenID Connect or ubirch token is missing")))

    } else {

      val deviceClaim = DeviceClaim(hwDeviceId = hwDeviceId, userId)
      Json4sUtil.any2String(deviceClaim) match {

        case Some(deviceClaimString) =>

          val url = AvatarRoutes.urlDeviceClaim
          val req = HttpRequest(
            method = HttpMethods.PUT,
            uri = url,
            headers = AuthUtil.authHeaders(oidcToken = None, ubirchToken = ubirchToken),
            entity = HttpEntity.Strict(ContentTypes.`application/json`, data = ByteString(deviceClaimString))
          )
          httpClient.singleRequest(req) flatMap {

            case HttpResponse(httpCode, _, entity, _) if httpCode.intValue() >= 200 && httpCode.intValue() < 300 =>

              entity.dataBytes.runFold(ByteString(""))(_ ++ _) map { body =>
                Right(read[DeviceUserClaim](body.utf8String))
              }

            case HttpResponse(StatusCodes.Forbidden, _, _, _) =>

              Future(Left(JsonErrorResponse(errorType = "InvalidToken", errorMessage = "login token is not valid")))

            case res@HttpResponse(code, _, entity, _) =>

              logger.warn(s"claimDevicePUT() call to avatar-service failed: url=$url code=$code, status=${res.status}")
              entity.dataBytes.runFold(ByteString(""))(_ ++ _) map { body =>
                Left(read[JsonErrorResponse](body.utf8String))
              }

          }

        case None =>

          logger.error(s"failed to to convert input to JSON: hwDeviceId=$hwDeviceId")
          Future(Left(JsonErrorResponse(errorType = "RestClientError", errorMessage = "error before sending the request: failed to convert input to JSON")))

      }
    }
  }


}
