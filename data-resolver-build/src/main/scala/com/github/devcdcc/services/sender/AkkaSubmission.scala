package com.github.devcdcc.services.sender
import io.circe.Json
import akka.actor._
object AkkaSubmission extends Submission {

  private class LocalActor extends Actor {

    // create the remote actor
    val remote: ActorSelection = context.actorSelection(
      "akka://ApiActorSystem@127.0.0.1:5150/user/ApiActor"
    )
    var counter = 0

    def receive: Receive = {
      case value =>
        remote ! value
    }
  }

  lazy implicit val system: ActorSystem = ActorSystem("LocalSystem")
  private lazy val localActor: ActorRef =
    system.actorOf(Props[LocalActor], name = "LocalActor")
  override def send(json: Json): Unit = localActor ! json.noSpaces
}
