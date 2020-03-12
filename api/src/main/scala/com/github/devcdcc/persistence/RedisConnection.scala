package com.github.devcdcc.persistence

import java.net.InetSocketAddress
import java.util.concurrent.Executors

import com.redis._
import scala.concurrent.{ExecutionContext, Future}
class RedisConnection(val address: InetSocketAddress) {
  val redis = new RedisClient(address.getHostName, address.getPort)
  def get(key: String): Option[String] = redis.get[String](key)
}
