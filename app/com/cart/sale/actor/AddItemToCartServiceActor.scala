package com.cart.sale.actor

import java.util.UUID
import java.util.concurrent.TimeUnit
import javax.inject.Inject

import akka.actor.{Actor, ActorLogging, Props}
import akka.pattern.ask
import akka.routing.RoundRobinPool
import akka.util.Timeout
import com.cart.sale.actor.AddItemToCartServiceActor.AddItemToCartUI
import com.cart.sale.actor.CachedAddItemToCartActor.AddItemToCart
import com.cart.sale.model._
import com.google.inject.Injector

import scala.util.{Failure, Success}

object AddItemToCartServiceActor {
  def props(in: Injector): Props = Props.create(classOf[AddItemToCartServiceActor], in)

  case class AddItemToCartUI(id: Option[UUID], item: Seq[String])

}

class AddItemToCartServiceActor @Inject()(in: Injector) extends Actor with ActorLogging {
  implicit val timeout: Timeout = Timeout(1, TimeUnit.SECONDS)
  private val cpa = context.actorOf(RoundRobinPool(5).props(CachedAddItemToCartActor.props(in)), "add-to-cart-router")

  override def receive: Receive = {
    case AddItemToCartUI(id, items) => addToCart(id, items)
  }

  def addToCart(id: Option[UUID], items: Seq[String]): Unit = {
    log.info(s"Trying to add $items for cart $id")

    val origSender = sender
    (cpa ? AddItemToCart(id, items)).map {
      case c: Cart =>
        c.items match {
          case item: Some[Seq[CartItem]] =>
            val cui = item.get.map(ci => CartItemUI(ci.id, PriceUI("0.00"))).seq
            log.info(s"Created cart is $cui")
            origSender ! CartUI(c.id, cui)
            cui
          case _ => log.error("Something happened...")
        }
      case _ => log.error("Something happened...")
    }.onComplete { // To track if the future ended in success or failure
      case Success(value) => log.debug(s"Successfully completed processing for $value")
      case Failure(e) => log.error(s"Something bad ${e.getMessage}", e)
    }
  }
}
