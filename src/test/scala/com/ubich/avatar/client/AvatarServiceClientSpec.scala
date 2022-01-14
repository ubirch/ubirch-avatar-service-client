package com.ubich.avatar.client

import akka.actor.ActorSystem
import akka.http.scaladsl.{Http, HttpExt}
import akka.stream.ActorMaterializer
import com.ubich.avatar.client.conf.Config
import com.ubich.avatar.client.model.{DummyDeviceDataRaw, DummyDevices}
import com.ubirch.avatar.client.AvatarServiceClient
import com.ubirch.util.deepCheck.model.DeepCheckResponse
import com.ubirch.util.model.{JsonErrorResponse, JsonResponse}
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
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val httpClient: HttpExt = Http()

  override protected def afterAll(): Unit = {
    super.afterAll()
    system.terminate()
    httpClient.shutdownAllConnectionPools()
    Thread.sleep(500)
    System.exit(0)
  }

  // TODO add database clean up before each test (elasticsearch, redis, neo4j and mongo)

  Feature("check()") {

    Scenario("check without errors") {

      // test
      AvatarServiceClient.check() map {

        // verify
        case None => fail("expected a result other than None")

        case Some(jsonResponse: JsonResponse) =>
          val goInfo = s"${Config.goPipelineName} / ${Config.goPipelineLabel} / ${Config.goPipelineRevision}"
          val expected = JsonResponse(message = s"Welcome to the ubirchAvatarService ( $goInfo )")
          jsonResponse.message.startsWith("Welcome to the ubirchAvatarService (") shouldBe true
          jsonResponse.status shouldBe expected.status
          jsonResponse.version shouldBe expected.version

      }

    }

  }

  Feature("deepCheck()") {

    Scenario("check without errors") {

      // test
      AvatarServiceClient.deepCheck() map { deepCheckResponse =>

        // verify
        assert(deepCheckResponse.isInstanceOf[DeepCheckResponse])
        deepCheckResponse.status shouldBe true

      }
    }
  }

  /*
  Feature("deviceUpdatePOST()") {

    // TODO add tests

  }
  */

  Feature("deviceUpdateBulkPOST()") {

    Scenario("without any token --> error") {

      // prepare
      val device = DummyDevices.device()
      val deviceData = DummyDeviceDataRaw.data(device = device)

      // test
      AvatarServiceClient.deviceUpdateBulkPOST(deviceDataRaw = deviceData) map {

        case Right(jsonResponse: JsonResponse) =>

          jsonResponse shouldBe JsonResponse(message = "processing started")

        case _ =>

          fail("request should have produced an error")

      }

    }

  }

  Feature("devicePOST()") {

    Scenario("without any token --> error") {

      // prepare
      val device = DummyDevices.device()

      // test
      AvatarServiceClient.deviceIdPUT(device = device) map {

        case Left(error: JsonErrorResponse) =>

          error shouldBe JsonErrorResponse(errorType = "RestClientError", errorMessage = "error before sending the request: either an OpenID Connect or ubirch token is missing")

        case _ =>

          fail("request should have produced an error")

      }

    }

    Scenario("invalid auth (oidc) --> error") {

      // prepare
      val device = DummyDevices.device()

      // test
      AvatarServiceClient.deviceIdPUT(device = device, oidcToken = Some("invalid-token")) map {

        case Left(error: JsonErrorResponse) =>

          error shouldBe JsonErrorResponse(errorType = "InvalidToken", errorMessage = "login token is not valid")

        case _ =>

          fail("request should have produced an error")

      }

    }

    Scenario("invalid auth (ubirch token) --> error") {

      // prepare
      val device = DummyDevices.device()

      // test
      AvatarServiceClient.deviceIdPUT(device = device, ubirchToken = Some("invalid-token")) map {

        case Left(error: JsonErrorResponse) =>

          error shouldBe JsonErrorResponse(errorType = "InvalidToken", errorMessage = "login token is not valid")

        case _ =>

          fail("request should have produced an error")

      }

    }

    // TODO Scenario("valid auth (oidc)        ; device does not exist --> success") {}
    // TODO Scenario("valid auth (ubirch token); device does not exist --> success") {}

    // TODO Scenario("valid auth (oidc)        ; device exists --> success") {}
    // TODO Scenario("valid auth (ubirch token); device exists --> success") {}

  }

  Feature("deviceStubGET()") {

    Scenario("without any token --> error") {

      // test
      AvatarServiceClient.deviceStubGET() map {

        case Left(error: JsonErrorResponse) =>

          error shouldBe JsonErrorResponse(errorType = "RestClientError", errorMessage = "error before sending the request: either an OpenID Connect or ubirch token is missing")

        case _ =>

          fail("request should have produced an error")

      }

    }

    Scenario("invalid auth (oidc) --> error") {

      // test
      AvatarServiceClient.deviceStubGET(oidcToken = Some("invalid-token")) map {

        case Left(error: JsonErrorResponse) =>

          error shouldBe JsonErrorResponse(errorType = "InvalidToken", errorMessage = "login token is not valid")

        case _ =>

          fail("request should have produced an error")

      }

    }

    Scenario("invalid auth (ubirch token) --> error") {

      // test
      AvatarServiceClient.deviceStubGET(ubirchToken = Some("invalid-token")) map {

        case Left(error: JsonErrorResponse) =>

          error shouldBe JsonErrorResponse(errorType = "InvalidToken", errorMessage = "login token is not valid")

        case _ =>

          fail("request should have produced an error")

      }

    }

    // TODO Scenario("valid auth (oidc)        ; has no devices --> empty device stub list") {}
    // TODO Scenario("valid auth (ubirch token); has no devices --> empty device stub list") {}

    // TODO Scenario("valid auth (oidc)        ; has devices --> non-empty device stub list") {}
    // TODO Scenario("valid auth (ubirch token); has devices --> non-empty device stub list") {}

  }

  Feature("deviceGET()") {

    Scenario("without any token --> error") {

      // test
      AvatarServiceClient.deviceGET() map {

        case Left(error: JsonErrorResponse) =>

          error shouldBe JsonErrorResponse(errorType = "RestClientError", errorMessage = "error before sending the request: either an OpenID Connect or ubirch token is missing")

        case _ =>

          fail("request should have produced an error")

      }

    }

    Scenario("invalid auth (oidc) --> error") {

      // test
      AvatarServiceClient.deviceGET(oidcToken = Some("invalid-token")) map {

        case Left(error: JsonErrorResponse) =>

          error shouldBe JsonErrorResponse(errorType = "InvalidToken", errorMessage = "login token is not valid")

        case _ =>

          fail("request should have produced an error")

      }

    }

    Scenario("invalid auth (ubirch token) --> error") {

      // test
      AvatarServiceClient.deviceGET(ubirchToken = Some("invalid-token")) map {

        case Left(error: JsonErrorResponse) =>

          error shouldBe JsonErrorResponse(errorType = "InvalidToken", errorMessage = "login token is not valid")

        case _ =>

          fail("request should have produced an error")

      }

    }

    // TODO Scenario("valid auth (oidc);         has no devices --> empty device list") {}
    // TODO Scenario("valid auth (ubirch token); has no devices --> empty device list") {}

    // TODO Scenario("valid auth (oidc);         has devices --> non-empty device list") {}
    // TODO Scenario("valid auth (ubirch token); has devices --> non-empty device list") {}

  }

  Feature("deviceIdPUT()") {

    Scenario("without any token --> error") {

      // prepare
      val device = DummyDevices.device()

      // test
      AvatarServiceClient.deviceIdPUT(device = device) map {

        case Left(error: JsonErrorResponse) =>

          error shouldBe JsonErrorResponse(errorType = "RestClientError", errorMessage = "error before sending the request: either an OpenID Connect or ubirch token is missing")

        case _ =>

          fail("request should have produced an error")

      }

    }

    Scenario("invalid auth (oidc) --> error") {

      // prepare
      val device = DummyDevices.device()

      // test
      AvatarServiceClient.deviceIdPUT(device = device, oidcToken = Some("invalid-token")) map {

        case Left(error: JsonErrorResponse) =>

          error shouldBe JsonErrorResponse(errorType = "InvalidToken", errorMessage = "login token is not valid")

        case _ =>

          fail("request should have produced an error")

      }

    }

    Scenario("invalid auth (ubirch token) --> error") {

      // prepare
      val device = DummyDevices.device()

      // test
      AvatarServiceClient.deviceIdPUT(device = device, ubirchToken = Some("invalid-token")) map {

        case Left(error: JsonErrorResponse) =>

          error shouldBe JsonErrorResponse(errorType = "InvalidToken", errorMessage = "login token is not valid")

        case _ =>

          fail("request should have produced an error")

      }

    }

    // TODO Scenario("valid auth(oidc)        ; device does not exist --> error") {}
    // TODO Scenario("valid auth(ubirch token); device does not exist --> error") {}

    // TODO Scenario("valid auth (oidc); device exists and belongs to same user --> success") {}
    // TODO Scenario("valid auth (ubirch token); device exists and belongs to same user --> success") {}

    // TODO Scenario("valid auth (oidc)        ; device exists but belongs to another user --> error") {}
    // TODO Scenario("valid auth (ubirch token); device exists but belongs to another user --> error") {}

  }

  Feature("deviceIdDELETE()") {

    Scenario("without any token --> error") {

      // test
      AvatarServiceClient.deviceIdDELETE(deviceId = UUIDUtil.uuid) map {

        case Left(error: JsonErrorResponse) =>

          error shouldBe JsonErrorResponse(errorType = "RestClientError", errorMessage = "error before sending the request: either an OpenID Connect or ubirch token is missing")

        case _ =>

          fail("request should have produced an error")

      }

    }

    Scenario("invalid auth (oidc) --> error") {

      // test
      AvatarServiceClient.deviceIdDELETE(deviceId = UUIDUtil.uuid, oidcToken = Some("invalid-token")) map {

        case Left(error: JsonErrorResponse) =>

          error shouldBe JsonErrorResponse(errorType = "InvalidToken", errorMessage = "login token is not valid")

        case _ =>

          fail("request should have produced an error")

      }

    }

    Scenario("invalid auth (ubirch token) --> error") {

      // test
      AvatarServiceClient.deviceIdDELETE(deviceId = UUIDUtil.uuid, ubirchToken = Some("invalid-token")) map {

        case Left(error: JsonErrorResponse) =>

          error shouldBe JsonErrorResponse(errorType = "InvalidToken", errorMessage = "login token is not valid")

        case _ =>

          fail("request should have produced an error")

      }

    }

    // TODO Scenario("valid auth (oidc)        ; device does not exist --> success") {}
    // TODO Scenario("valid auth (ubirch token); device does not exist --> success") {}

    // TODO Scenario("valid auth (oidc)        ; device belongs to another user --> error") {}
    // TODO Scenario("valid auth (ubirch token); device belongs to another user --> error") {}

    // TODO Scenario("valid auth (oidc)        ; device belongs to same user --> success") {}
    // TODO Scenario("valid auth (ubirch token); device belongs to same user --> success") {}

  }

  Feature("claimDevicePUT()") {

    Scenario("without any token --> error") {

      // test
      AvatarServiceClient.claimDevicePUT(hwDeviceId = UUIDUtil.uuidStr) map {

        case Left(error: JsonErrorResponse) =>

          error shouldBe JsonErrorResponse(errorType = "RestClientError", errorMessage = "error before sending the request: either an OpenID Connect or ubirch token is missing")

        case _ =>

          fail("request should have produced an error")

      }

    }

    Scenario("invalid auth (oidc) --> error") {

      // test
      AvatarServiceClient.claimDevicePUT(hwDeviceId = UUIDUtil.uuidStr, oidcToken = Some("invalid-token")) map {

        case Left(error: JsonErrorResponse) =>

          error shouldBe JsonErrorResponse(errorType = "InvalidToken", errorMessage = "login token is not valid")

        case _ =>

          fail("request should have produced an error")

      }

    }

    Scenario("invalid auth (ubirch token) --> error") {

      // test
      AvatarServiceClient.claimDevicePUT(hwDeviceId = UUIDUtil.uuidStr, ubirchToken = Some("invalid-token")) map {

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
