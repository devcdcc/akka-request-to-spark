package com.github.devcdcc.idhandler

import java.util.UUID

class RequestIdGeneratorImpl extends RequestIdGenerator {
  override def generate: String = UUID.randomUUID().toString
}
