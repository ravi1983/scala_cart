import java.util.UUID

import com.cart.sale.model.{Cart, CartItem}

val s = Seq("1", "2", "3")
val opt = Option(s)

opt match {
  case i: Some[Seq[String]] => i.value.length
  case _ => "none"
}

val c1 = Cart(UUID.randomUUID())
c1.items match {
  case ff: Some[Seq[CartItem]] => "yes val"
  case None => "no val"
}

