package controllers.com.cart.sale

import com.cart.sale.util.RedisHelper
import com.google.inject.matcher.Matchers
import com.google.inject.spi.{InjectionListener, TypeEncounter, TypeListener}
import com.google.inject.{AbstractModule, TypeLiteral}

class RedisModule extends AbstractModule {
  override def configure(): Unit = {
    println("******************** HERE ****************")
    bindListener(Matchers.only(classOf[RedisHelper]), new TypeListener {
      override def hear[I](`type`: TypeLiteral[I], encounter: TypeEncounter[I]): Unit = {
        encounter.register(new InjectionListener[I] {
          override def afterInjection(injectee: I): Unit = {
            val rh = injectee.asInstanceOf[RedisHelper]
            rh.init
          }
        })
      }
    })
  }
}
