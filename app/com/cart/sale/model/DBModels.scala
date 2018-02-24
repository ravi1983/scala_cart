package com.cart.sale.model

import java.util.UUID

case class Cart(id: UUID, items: Option[Seq[CartItem]] = None, price: Option[Price] = None, tax: Option[String] = None, subtotal: Option[String] = None, total: Option[String] = None)

case class CartItem(id: String, price: Price)

case class Price(regular: String, sale: Option[String] = None)
