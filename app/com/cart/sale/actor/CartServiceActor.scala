package com.cart.sale.actor

import javax.inject.{Inject, Singleton}

import akka.actor.{Actor, Props}
import akka.pattern.ask
import akka.routing.RoundRobinPool
import com.cart.sale._
import com.cart.sale.actor.CachedPersistenceCartActor.CreateCart
import com.cart.sale.actor.CartServiceActor.CreateCartUI
import com.cart.sale.model._
import com.google.inject.Injector
import play.api.Logger

import scala.concurrent.Await

object CartServiceActor {

  def props(in: Injector): Props = Props.create(classOf[CartServiceActor], in)

  case class CreateCartUI(item: Seq[String])

}

@Singleton
class CartServiceActor @Inject()(in: Injector) extends Actor {
  val cpa = context.actorOf(RoundRobinPool(5).props(CachedPersistenceCartActor.props(in)), "persistence_router")

  private val log = Logger(getClass)

  override def receive = {
    case CreateCartUI(items) => createCart(items)
  }

  def createCart(items: Seq[String]) = {
    log.info(s"Creating cart with items $items")

    val future = (cpa ? CreateCart(items)).mapTo[Cart]
    Await.result(future, timeout.duration) match {
      case c: Cart =>
        val items = c.items
        if (items.nonEmpty) {
          val cui = items.get.map(ci => new CartItemUI(ci.id, PriceUI("0.00"))).seq
          log.info(s"Created cart is $cui")
          sender ! CartUI(c.id, cui)
        } else {
          sender ! CartUI(c.id)
        }
      case _ => log.error("Something happened...")
    }
  }
}
