package controllers.com.cart.sale

import javax.inject.Inject

import com.cart.sale.util.RedisHelper
import com.google.inject.AbstractModule
import play.api.Logger

class RedisModule extends AbstractModule {

  private val log = Logger(getClass)

  def configure(): Unit = {
    requestInjection(this)
  }

  @Inject
  def initRedisHelper(instance: RedisHelper): Unit = {
    instance.init
  }
}