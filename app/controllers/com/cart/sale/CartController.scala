package controllers.com.cart.sale

import javax.inject.{Inject, Named}

import akka.actor.{ActorRef, ActorSystem}
import akka.pattern.ask
import com.cart.sale._
import com.cart.sale.actor.CartServiceActor.CreateCartUI
import com.cart.sale.model.CartUI
import com.google.inject.Injector
import play.api.libs.json.Json
import play.api.mvc.InjectedController

import scala.concurrent.ExecutionContext.Implicits.global

class CartController @Inject()(aSys: ActorSystem, in: Injector, @Named("csActor") ca: ActorRef) extends InjectedController {

  def create = Action.async(parse.json) {
    request =>
      val body = request.body
      val item = (body \ "id").asOpt[Seq[String]]
      (ca ? CreateCartUI(item)).map { case data: CartUI => Ok(Json.toJson(data)) }
  }
}
