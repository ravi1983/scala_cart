val s = Seq("1", "2", "3")
val opt = Option(s)

opt match {
  case i: Some[Seq[String]] => i.value.length
  case _ => "none"
}

