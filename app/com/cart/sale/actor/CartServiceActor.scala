package com.cart.sale.actor

import java.util.UUID
import javax.inject.{Inject, Singleton}

import akka.actor.{Actor, ActorLogging, Props}
import akka.pattern.ask
import akka.routing.RoundRobinPool
import com.cart.sale._
import com.cart.sale.actor.CachedPersistenceCartActor.CreateCart
import com.cart.sale.actor.CartServiceActor.CreateCartUI
import com.cart.sale.model._
import com.google.inject.Injector

import scala.concurrent.ExecutionContext.Implicits.global

object CartServiceActor {

  def props(in: Injector): Props = Props.create(classOf[CartServiceActor], in)

  case class CreateCartUI(item: Option[Seq[String]])

}

@Singleton
class CartServiceActor @Inject()(in: Injector) extends Actor with ActorLogging {
  val cpa = context.actorOf(RoundRobinPool(5).props(CachedPersistenceCartActor.props(in)), "persistence_router")

  override def receive = {
    case CreateCartUI(items) => createCart(items)
  }

  def createCart(items: Option[Seq[String]]) = {
    log.info(s"Creating cart with items $items")

    val origSender = sender
    (cpa ? CreateCart(items)).map {
      case c: Cart =>
        c.items match {
          case item: Some[Seq[CartItem]] =>
            val cui = item.get.map(ci => CartItemUI(ci.id, PriceUI("0.00"))).seq
            log.info(s"Created cart is $cui")
            origSender ! CartUI(c.id, cui)
          case _ => origSender ! CartUI(c.id)
        }
      case _ => log.error("Something happened...")
    }
  }
}
