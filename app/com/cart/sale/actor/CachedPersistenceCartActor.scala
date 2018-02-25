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

  case class CreateCart(items: Option[Seq[String]])

}

@Singleton
class CachedPersistenceCartActor @Inject()(in: Injector) extends Actor {
  private val log = Logger(getClass)

  val rh = in.getInstance(classOf[RedisHelper])

  override def receive = {
    case CreateCart(items) =>
      log.info("Creating cart...")

      items match {
        case item: Some[Seq[String]] =>
          val cis = item.get.map(ci => CartItem(ci, Price("0.00"))).seq
          val cart = Cart(UUID.randomUUID(), Some(cis))
          processCart(cart)
        case _ =>
          val cart = Cart(UUID.randomUUID())
          processCart(cart)
      }

      def processCart(cart: Cart) = {
        log.debug(s"Cart to be stored is $cart")
        rh.save(cart.id.toString, cart)
        sender ! cart
      }
  }
}
