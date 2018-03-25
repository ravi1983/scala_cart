package controllers.com.cart.sale

import javax.inject.{Named, Singleton}

import akka.actor.{ActorRef, ActorSystem}
import akka.routing.RoundRobinPool
import com.cart.sale.actor.CartServiceActor
import com.google.inject.{AbstractModule, Injector, Provides}

class ActorModule extends AbstractModule {
  override def configure(): Unit = {

  }

  @Singleton
  @Provides
  @Named("csActor")
  def crateCSActor(injector: Injector): ActorRef = {
    val as = injector.getInstance(classOf[ActorSystem])
    as.actorOf(RoundRobinPool(3).props(CartServiceActor.props(injector)), "Cart-Actor-Router")
  }

  @Singleton
  @Provides
  @Named("addToCartActor")
  def crateAddToCartActor(injector: Injector): ActorRef = {
    val as = injector.getInstance(classOf[ActorSystem])
    as.actorOf(RoundRobinPool(5).props(CartServiceActor.props(injector)), "Add-To-Cart-Actor-Router")
  }
}
