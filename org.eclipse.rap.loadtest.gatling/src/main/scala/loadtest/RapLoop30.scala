/*******************************************************************************
 * Copyright (c) 2015 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    EclipseSource - initial API and implementation
 ******************************************************************************/
package loadtest

import scala.concurrent.duration._
import io.gatling.core.Predef._
import io.gatling.http.Predef._


class RapLoop30 extends Simulation {

  val displayId = "w1"
  val shellId = "w2"
  val textId = "w104"
  val buttonId = "w105"

  val baseUrl = "http://127.0.0.1:8080"
  val basePath = "/rap-loadtest-3.0.0/"
  val headers = Map("Pragma" -> "no-cache")

  val httpProtocol = http.baseURL(baseUrl).inferHtmlResources()

  val scn = scenario("RapSimulation")
    .repeat(100, "count") {
      uiRequest()
    }

  setUp(scn.inject(rampUsers(1000) over(60 seconds))).protocols(httpProtocol)

  def uiRequest() = {
    exec(session => {
      session.set("req_ops", getReqOps(session))
    })
    .doIfOrElse(session => session("count").as[Int] == 0) {
      initialRequest()
    } {
      subsequentRequest()
    }
//    .exec(session => {
//      println("-----")
//      println(session)
//      println("-----")
//      session
//    })
  }

  def initialRequest() = {
    exec(http("initial request")
      .post(basePath)
      .headers(headers)
      .body(StringBody("""{"head":{"requestCounter":${count}},"operations":${req_ops}}""")).asJSON
      .check(status.is(200))
      .check(jsonPath("$.head.cid").saveAs("cid"))
      .check(jsonPath("$.operations").saveAs("res_ops")))
    .pause(2)
  }

  def subsequentRequest() = {
    exec(http("request ${count}")
      .post(basePath + "?cid=${cid}")
      .headers(headers)
      .body(StringBody("""{"head":{"requestCounter":${count}},"operations":${req_ops}}""")).asJSON
      .check(status.is(200))
      .check(jsonPath("$.operations").saveAs("res_ops")))
    .pause(1)
  }

  // TODO text size probe result extracted from browser tools
  // ensure that text size caches are filled before test runs
  var opInitial = s"""[
    ["set", "${displayId}", {
      "bounds": [0, 0, 1200, 800],
      "dpi": [96, 96],
      "colorDepth": 32,
      "cursorLocation": [0, 0]
    }],
    ["set", "rwt.client.ClientInfo", {
      "timezoneOffset": 0
    }],
    ["call", "rwt.client.TextSizeMeasurement", "storeMeasurements", {
      "results": {"p-785380997": [766, 18]}
    }]
  ]"""

  var opModifyText1 = s"""[
    ["set", "${textId}", {
      "selection": [3, 3],
      "text": "foo"
    }],
    ["set", "${displayId}", {
      "cursorLocation": [23, 42],
      "focusControl":"${textId}"
    }],
    ["notify", "${textId}", "Modify", {}]
  ]"""

  var opModifyText2 = s"""[
    ["set", "${textId}", {
      "selection": [5, 5],
      "text": "foo23"
    }],
    ["set", "${displayId}", {
      "cursorLocation": [25, 47]
    }],
    ["notify", "${textId}", "Modify", {}]
  ]"""

  var opClearText = s"""[
    ["set", "${shellId}", {
      "activeControl": "${buttonId}"
    }],
    ["notify", "${buttonId}", "Selection", {
      "button": 1,
      "shiftKey": false,
      "ctrlKey": false,
      "altKey": false
    }],
    ["set", "${displayId}", {
      "cursorLocation": [23, 42],
      "focusControl": "${buttonId}"
    }]
  ]"""

  def getReqOps(session: Session) = {
    val count = session("count").as[Int]
    val ops = if (count == 0)
      opInitial
    else if (count % 3 == 1)
      opModifyText1
    else if (count % 3 == 2)
      opModifyText2
    else
      opClearText
    ops.replaceAll("\\s+", "")
  }

}
