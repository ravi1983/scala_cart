package com.cart.sale.util

import javax.inject.Singleton

import akka.actor.ActorSystem
import akka.util.ByteString
import com.cart.sale.model.Cart
import com.google.inject.Inject
import play.api.Logger
import play.libs.Json
import redis.{ByteStringDeserializer, ByteStringSerializer, SentinelMonitoredRedisClient}

import scala.concurrent.Future

@Singleton
class RedisHelper @Inject()(aSys: ActorSystem) {

  private val log = Logger(getClass)
  var redis: SentinelMonitoredRedisClient = _

  implicit val akkaSystem: ActorSystem = aSys
  implicit val serString: ByteStringSerializer[String] = (data: String) => ByteString(data)
  implicit val serAny: ByteStringSerializer[Any] = (data: Any) => ByteString(Json.toJson(data).asText())
  implicit val deSerAny: ByteStringDeserializer[Cart] = (bs: ByteString) => Json.fromJson[Cart](Json.parse(bs.utf8String), classOf[Cart])

  def init {
    redis = SentinelMonitoredRedisClient(Seq(("localhost", 26179), ("localhost", 26381)), "redis-cluster")
  }

  def save(key: String, value: Any): Future[Boolean] = {
    redis.set(key, value, Some(60))
  }

  def get(key: String): Future[Option[Cart]] = {
    redis.get[Cart](key)
  }
}