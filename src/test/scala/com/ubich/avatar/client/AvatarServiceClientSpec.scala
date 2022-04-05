package com.ubich.avatar.client

import akka.actor.ActorSystem
import akka.http.scaladsl.{Http, HttpExt}
import com.ubirch.avatar.client.AvatarServiceClient
import com.ubirch.util.model.JsonErrorResponse
import com.ubirch.util.uuid.UUIDUtil
import org.scalatest.featurespec.AsyncFeatureSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach}

import scala.concurrent.ExecutionContextExecutor

class AvatarServiceClientSpec extends AsyncFeatureSpec
  with Matchers
  with BeforeAndAfterEach
  with BeforeAndAfterAll {

  implicit val system: ActorSystem = ActorSystem()
  implicit val ec: ExecutionContextExecutor = system.dispatcher
  implicit val httpClient: HttpExt = Http()

  override protected def afterAll(): Unit = {
    super.afterAll()
    system.terminate()
    httpClient.shutdownAllConnectionPools()
    Thread.sleep(500)
    System.exit(0)
  }

  // TODO add database clean up before each test (elasticsearch, redis, neo4j and mongo)



  Feature("claimDevicePUT()") {

    Scenario("without any token --> error") {

      // test
      AvatarServiceClient.claimDevicePUT(UUIDUtil.uuid, hwDeviceId = UUIDUtil.uuidStr) map {

        case Left(error: JsonErrorResponse) =>

          error shouldBe JsonErrorResponse(errorType = "RestClientError", errorMessage = "error before sending the request: either an OpenID Connect or ubirch token is missing")

        case _ =>

          fail("request should have produced an error")

      }

    }

    Scenario("invalid auth (oidc) --> error") {

      // test
      AvatarServiceClient.claimDevicePUT(UUIDUtil.uuid, hwDeviceId = UUIDUtil.uuidStr, ubirchToken = Some("invalid-token")) map {

        case Left(error: JsonErrorResponse) =>

          error shouldBe JsonErrorResponse(errorType = "InvalidToken", errorMessage = "login token is not valid")

        case _ =>

          fail("request should have produced an error")

      }

    }

    Scenario("invalid auth (ubirch token) --> error") {

      // test
      AvatarServiceClient.claimDevicePUT(UUIDUtil.uuid, hwDeviceId = UUIDUtil.uuidStr, ubirchToken = Some("invalid-token")) map {

        case Left(error: JsonErrorResponse) =>

          error shouldBe JsonErrorResponse(errorType = "InvalidToken", errorMessage = "login token is not valid")

        case _ =>

          fail("request should have produced an error")

      }

    }

    // TODO Scenario("valid auth(oidc)        ; device does not exist --> error") {}
    // TODO Scenario("valid auth(ubirch token); device does not exist --> error") {}

    // TODO Scenario("valid auth (oidc)        ; device has been claimed already --> ???") {}
    // TODO Scenario("valid auth (ubirch token); device has been claimed already --> ???") {}

    // TODO Scenario("valid auth (oidc)        ; device has not been claimed --> success") {}
    // TODO Scenario("valid auth (ubirch token); device has not been claimed --> success") {}

  }

}
