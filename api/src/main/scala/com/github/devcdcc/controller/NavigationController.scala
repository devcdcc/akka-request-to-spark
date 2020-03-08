package com.github.devcdcc.controller

import akka.http.scaladsl.model._
import de.heikoseeberger.akkahttpcirce._
import akka.http.scaladsl.server.Directives._
import FailFastCirceSupport._
import akka.http.scaladsl.server.Route
import com.github.devcdcc.utils.circe.auto._
import com.github.devcdcc.request._
import io.circe.syntax._
trait NavigationController {
  lazy val route: Route = path("navigation") {
    get {
      entity(as[DateRange]){
        case DateRange(start, end) =>complete {

        }
      }
    }
  }
}
