package loadtest

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._


class RapLoop30 extends Simulation {

  val httpProtocol = http
    .baseURL("http://10.233.145.246:8080")
    .inferHtmlResources()

  val headers = Map("Pragma" -> "no-cache")

  val scn = scenario("RapSimulation")
    .repeat(100, "count") {
      uiRequest()
    }

  setUp(scn.inject(rampUsers(1000) over(100 seconds))).protocols(httpProtocol)

  def uiRequest() = {
    doIfOrElse(session => session("count").as[Int] == 0) {
      initialRequest()
    } {
      subsequentRequest()
    }
  }

  def initialRequest() = {
    exec(http("initial request")
      .post("/rap-loadtest-3.0/")
      .headers(headers)
      .body(StringBody("""{"head":{"requestCounter":${count}},"operations":[]}""")).asJSON
      .check(status.is(200))
      .check(jsonPath("$.head.cid").saveAs("cid")))
    .pause(120)
  }

  def subsequentRequest() = {
    exec(http("request ${count}")
      .post("/rap-loadtest-3.0/?cid=${cid}")
      .headers(headers)
      .body(StringBody("""{"head":{"requestCounter":${count}},"operations":[]}""")).asJSON
      .check(status.is(200)))
    .pause(1)
  }

}
