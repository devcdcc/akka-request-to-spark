package com.github.devcdcc.services.sender

import io.circe.Json

trait Submission {
  def send(json: Json): Unit
}
