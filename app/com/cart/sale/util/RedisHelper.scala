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

  implicit val akkaSystem = aSys

  implicit val serString = new ByteStringSerializer[String] {
    override def serialize(data: String): ByteString = {
      ByteString(data)
    }
  }

  implicit val serAny = new ByteStringSerializer[Any] {
    override def serialize(data: Any): ByteString = {
      ByteString(Json.toJson(data).asText())
    }
  }

  implicit val deSerAny = new ByteStringDeserializer[Cart] {
    override def deserialize(bs: ByteString): Cart = {
      Json.fromJson[Cart](Json.parse(bs.utf8String), classOf[Cart])
    }
  }

  def init {
    redis = new SentinelMonitoredRedisClient(Seq(("localhost", 26179), ("localhost", 26381)), "redis-cluster")
    redis.ping()
  }

  def save(key: String, value: Any): Future[Boolean] = {
    redis.set(key, value, Some(60))
  }

  def get(key: String): Future[Option[Cart]] = {
    redis.get[Cart](key)
  }
}