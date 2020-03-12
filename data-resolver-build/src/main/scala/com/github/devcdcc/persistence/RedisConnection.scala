package com.github.devcdcc.persistence

import java.net.InetSocketAddress
import java.util.concurrent.Executors

import com.redis._
import scala.concurrent.{ExecutionContext, Future}
class RedisConnection(val address: InetSocketAddress) {
  implicit val ec =
    ExecutionContext.fromExecutor(Executors.newFixedThreadPool(2))
  val redis = new RedisClient(address.getHostName, address.getPort)

  def setKey(key: String, value: String): Boolean =
    redis.set(key, value)
}
