package controllers.com.cart.sale

import javax.inject.Inject

import akka.actor.ActorSystem
import akka.pattern.ask
import akka.routing.RoundRobinPool
import com.cart.sale._
import com.cart.sale.actor.CartServiceActor
import com.cart.sale.actor.CartServiceActor.CreateCartUI
import com.cart.sale.model.CartUI
import com.google.inject.Injector
import play.api.Logger
import play.api.libs.json.Json
import play.api.mvc.InjectedController

import scala.concurrent.ExecutionContext.Implicits.global

class CartController @Inject()(aSys: ActorSystem, in: Injector) extends InjectedController {

  private val log = Logger(getClass)

  private val csa = aSys.actorOf(RoundRobinPool(3).props(CartServiceActor.props(in)), "Cart-Actor-Router")

  def create = Action.async(parse.json) {
    request =>
      val body = request.body
      val item = (body \ "id").as[Seq[String]] // TODO Change to asOpt
      (csa ? CreateCartUI(item)).map { case cart: CartUI => Ok(Json.toJson(cart)) }
  }
}
