package controllers
import java.util.Calendar

import scala.concurrent.Future
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import play.api.mvc._
import play.api.libs.ws._
import play.api.http.HttpEntity
import akka.actor.ActorSystem
import akka.stream.scaladsl._
import akka.util.ByteString
import com.google.inject.Inject
import play.api.libs.json.{JsArray, JsNull, JsObject, Json, JsValue}

import scala.collection.mutable

/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
class HomeController @Inject()(ws: WSClient,
                               val controllerComponents: ControllerComponents)
    extends BaseController {
  import LagashConstants._
  val json: JsValue = Json.obj(
    "name" -> "Watership Down",
    "location" -> Json.obj("lat" -> 51.235685, "long" -> -1.309197),
    "residents" -> Json.arr(
      Json.obj("name" -> "Fiver", "age" -> 4, "role" -> JsNull),
      Json.obj("name" -> "Bigwig", "age" -> 6, "role" -> "Owsla")
    )
  )

  /**
    * Create an Action to render an HTML page.
    *
    * The configuration in the `routes` file means that this method
    * will be called when the application receives a `GET` request with
    * a path of `/`.
    */
  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(json)
  }

  val requestWS: WSRequest = ws.url(LAGASH_URL)
  val complexRequest: WSRequest =
    requestWS
      .addHttpHeaders("Accept" -> "application/json")
      .addHttpHeaders("x-api-key" -> "hola")
      .withRequestTimeout(10000.millis)

  def getMaxScore() = Action.async { implicit request: Request[AnyContent] =>
    val timestamp = getCurrentTimestamp
    val response = complexRequest.execute().map(response => response.json).map {
      case JsArray(value) =>
        value
          .map(_.as[JsObject])
          .filter { customer =>
            val id = customer.value("id").toString()
            val lastTimestamp: Long = map.getOrElse(id, 0l)
            if (lastTimestamp > 0) {
              lastTimestamp + 5 * 1000 < timestamp
            } else true
          }
          .maxBy(
            customer =>
              customer
                .value("score")
                .toString()
          )
    }
    response.foreach(
      customer => map.addOne(customer.value("id").toString(), timestamp)
    )
    response.map(value => Ok(value))
  }
  private def getCurrentTimestamp = Calendar.getInstance().getTimeInMillis
}
object LagashConstants {
  final val LAGASH_URL = "http://interview.lagash.com.ar/api/customer/byScore"

  val map = mutable.HashMap.empty[String, Long]
}
