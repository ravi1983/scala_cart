package com.cart.sale.model

import java.util.UUID

case class CartUI(id: UUID, items: Seq[CartItemUI] = Seq(), tax: String = "0.00", subtotal: String = "0.00", total: String = "0.00")

case class CartItemUI(id: String, price: PriceUI)

case class PriceUI(regular: String, sale: Option[String] = None)
