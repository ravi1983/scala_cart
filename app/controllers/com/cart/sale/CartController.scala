package controllers.com.cart.sale

import java.util.UUID
import javax.inject.{Inject, Named}

import akka.actor.{ActorRef, ActorSystem}
import akka.pattern.ask
import com.cart.sale._
import com.cart.sale.actor.AddItemToCartServiceActor.AddItemToCartUI
import com.cart.sale.actor.CartServiceActor.CreateCartUI
import com.cart.sale.model.CartUI
import com.google.inject.Injector
import play.api.libs.json.Json
import play.api.mvc.InjectedController

import scala.concurrent.ExecutionContext.Implicits.global

class CartController @Inject()(aSys: ActorSystem, in: Injector,
                               @Named("csActor") ca: ActorRef,
                               @Named("addToCartActor") aca: ActorRef)
  extends InjectedController {

  def create = Action.async(parse.json) {
    request =>
      val body = request.body
      val item = (body \ "items").asOpt[Seq[String]]
      (ca ? CreateCartUI(item)).map { case data: CartUI => Ok(Json.toJson(data)) }
  }

  def add = Action.async(parse.json) {
    request =>
      val body = request.body
      val id = (body \ "id").asOpt[UUID]
      val item = (body \ "items").as[Seq[String]]
      (aca ? AddItemToCartUI(id, item)).map { case data: CartUI => Ok(Json.toJson(data)) }
  }
}
