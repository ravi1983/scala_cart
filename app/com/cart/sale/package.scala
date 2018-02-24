package com.cart

import java.util.concurrent.TimeUnit

import akka.util.Timeout
import com.cart.sale.model.{CartItemUI, CartUI, PriceUI}
import play.api.libs.json.{Json, OWrites, Writes}

package object sale {

  implicit val timeout: Timeout = Timeout(1, TimeUnit.SECONDS)

  implicit val pFormat: OWrites[PriceUI] = Json.writes[PriceUI]
  implicit val ciFormat: OWrites[CartItemUI] = Json.writes[CartItemUI]

  implicit val ccFormat = new Writes[CartUI] {
    def writes(ci: CartUI) = Json.obj(
      "id" -> ci.id,
      "items" -> ci.items
    )
  }
}
