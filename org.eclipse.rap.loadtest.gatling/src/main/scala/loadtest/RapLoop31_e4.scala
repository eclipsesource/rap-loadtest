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


class RapLoop31_e4 extends Simulation {

  val displayId = "w1"

  val baseUrl = "http://127.0.0.1:42527"
  val basePath = "/hello"
  val headers = Map("Pragma" -> "no-cache")

  val httpProtocol = http.baseURL(baseUrl).inferHtmlResources()

  val scn = scenario("RapSimulation")
    .repeat(50, "count") {
      uiRequest()
    }

  setUp(scn.inject(rampUsers(200) over(60 seconds))).protocols(httpProtocol)

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
      "results": {"p-785380997": [768, 17], "p-785379973": [549, 12]}
    }]
  ]"""

  var opLogin = s"""[
   ["set","w2",{"activeControl":"w9"}],
   ["notify","w9","Selection",{"button":1,"shiftKey":false,"ctrlKey":false,"altKey":false}],
   ["set","w1",{"cursorLocation":[943,246],"focusControl":"w9"}]
  ]"""

  var opMaximize = s"""[
    ["set","w12",{"activeControl":"w12","mode":"maximized","bounds":[0,0,1680,390]}],
    ["notify","w12","Resize",{}],
    ["set","w1",{"cursorLocation":[457,25],"focusControl":"w12"}]
  ]"""

  var opSelectSharedView = s"""[
    ["notify","w17","FocusIn",{}],
    ["set","w12",{"activeControl":"w17"}],
    ["notify","w17","Activate",{}],
    ["notify","w17","MouseDown",{"x":215,"y":113,"time":46100,"button":1,"shiftKey":false,"ctrlKey":false,"altKey":false}],
    ["notify","w17","MouseUp",{"x":215,"y":113,"time":46133,"button":1,"shiftKey":false,"ctrlKey":false,"altKey":false}],
    ["set","w17",{"selection":"w56"}],
    ["notify","w17","Selection",{"item":"w56","button":1,"shiftKey":false,"ctrlKey":false,"altKey":false}],
    ["set","w1",{"cursorLocation":[215,113],"focusControl":"w17"}]
  ]"""

  var opSelectEmptyView = s"""[
    ["notify","w17","FocusIn",{}],
    ["set","w12",{"activeControl":"w17"}],
    ["notify","w17","Activate",{}],
    ["notify","w17","MouseDown",{"x":77,"y":115,"time":231904,"button":1,"shiftKey":false,"ctrlKey":false,"altKey":false}],
    ["notify","w17","MouseUp",{"x":77,"y":115,"time":231940,"button":1,"shiftKey":false,"ctrlKey":false,"altKey":false}],
    ["set","w17",{"selection":"w55"}],
    ["notify","w17","Selection",{"item":"w55","button":1,"shiftKey":false,"ctrlKey":false,"altKey":false}],
    ["set","w1",{"cursorLocation":[77,115],"focusControl":"w17"}]
  ]"""

  def getReqOps(session: Session) = {
    val count = session("count").as[Int]
    val ops = if (count == 0)
      opInitial
    else if (count == 1)
      opLogin
    else if (count == 2)
      opMaximize
    else if (count % 2 == 1)
      opSelectSharedView
    else
      opSelectEmptyView
    ops.replaceAll("\\s+", "")
  }

}
