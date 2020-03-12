package com.github.devcdcc.idhandler

trait RequestIdGenerator {
  def generate: String
}
