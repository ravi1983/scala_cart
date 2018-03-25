package com.cart.sale.actor

import java.util.UUID
import javax.inject.Inject

import akka.actor.{Actor, ActorLogging, Props}
import com.cart.sale.util.RedisHelper
import com.google.inject.Injector

object CachedAddItemToCartActor {
  def props(in: Injector): Props = Props.create(classOf[AddItemToCartServiceActor], in)

  case class AddItemToCart(id: Option[UUID], item: Seq[String])

}

class CachedAddItemToCartActor @Inject()(in: Injector) extends Actor with ActorLogging {

  val rh = in.getInstance(classOf[RedisHelper])

  override def receive: Receive = {

  }
}
