package com.cart.sale.actor

import java.util.UUID
import javax.inject.{Inject, Singleton}

import akka.actor.{Actor, Props}
import com.cart.sale.actor.CachedPersistenceCartActor.CreateCart
import com.cart.sale.model.{Cart, CartItem, Price}
import com.cart.sale.util.RedisHelper
import com.google.inject.Injector
import play.api.Logger

object CachedPersistenceCartActor {

  def props(in: Injector): Props = Props.create(classOf[CachedPersistenceCartActor], in)

  case class CreateCart(items: Seq[String])

}

@Singleton
class CachedPersistenceCartActor @Inject()(in: Injector) extends Actor {
  private val log = Logger(getClass)

  override def receive = {
    case CreateCart(items) =>
      log.info("Creating cart...")

      val rh = in.getInstance(classOf[RedisHelper])
      log.info(s"Do stuff $rh")
      val cis = items.map(ci => CartItem(ci, Price("0.00"))).seq
      sender ! Cart(UUID.randomUUID(), Some(cis))
  }
}
