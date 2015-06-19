package loadtest

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._


class RapLoop extends Simulation {

  val httpProtocol = http
    .baseURL("http://127.0.0.1:8080")
    .inferHtmlResources()

  val headers = Map("Pragma" -> "no-cache")

  val scn = scenario("RapSimulation")
    .repeat(10, "count") {
      uiRequest()
      .pause(1)
    }

  setUp(scn.inject(atOnceUsers(5))).protocols(httpProtocol)

  def uiRequest() = {
    exec(session => {
      val count = session("count").as[Int]
      val url = if (count == 0) "/rap" else "/rap?cid=" + session("cid").as[String]
      session.set("url", url)
    })
    .doIfOrElse(session => session("count").as[Int] == 0) {
      exec(http("request ${count}")
        .post("${url}")
        .headers(headers)
        .body(StringBody("""{"head":{"requestCounter":${count}},"operations":[]}""")).asJSON
        .check(status.is(200))
        .check(jsonPath("$.head.cid").saveAs("cid")))
    } {
      exec(http("request ${count}")
        .post("${url}")
        .headers(headers)
        .body(StringBody("""{"head":{"requestCounter":${count}},"operations":[]}""")).asJSON
        .check(status.is(200)))
    }
  }

  def initialRequest() = {
    http("request")
      .post("/rap")
      .headers(headers)
      .body(StringBody("""{"head":{"requestCounter":${count}},"operations":[]}""")).asJSON
      .check(status.is(200))
      .check(jsonPath("$.head.cid").saveAs("cid"))
  }

  def subsequentRequest() = {
    http("request ${count}")
      .post("/rap?cid=${cid}")
      .headers(headers)
      .body(StringBody("""{"head":{"requestCounter":${count}},"operations":[]}""")).asJSON
      .check(status.is(200))
  }

}
