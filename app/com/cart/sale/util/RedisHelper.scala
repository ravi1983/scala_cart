package com.cart.sale.util

import javax.inject.Singleton

import akka.actor.ActorSystem
import com.google.inject.Inject
import play.api.Logger
import redis.SentinelMonitoredRedisClient

@Singleton
class RedisHelper @Inject()(aSys: ActorSystem) {

  private val log = Logger(getClass)
  var redis: SentinelMonitoredRedisClient = _

  implicit val akkaSystem = aSys

  def init: Unit = {
    log.info("**************** setup redis ****************")
    redis = new SentinelMonitoredRedisClient(Seq(("localhost", 26179), ("localhost", 26381)), "redis-cluster")
  }

  //  def save(key: String, value: AnyVal): Future[Boolean] = {
  //    redis.set(key, value, Some(10))
  //  }
  //
  //  def get[T](key: String, value: AnyVal): Future[Boolean] = {
  //    redis.set(key, value, Some(10))
  //  }
}