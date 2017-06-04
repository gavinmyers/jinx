package game

/**
  * Created by gavin on 6/4/17.
  */
object BodyParts extends Enumeration with java.io.Serializable {
  type BodyParts = Value
  val LEFT_HAND = Value("LEFT_HAND")
  val RIGHT_HAND = Value("RIGHT_HAND")
  val HEAD = Value("HEAD")
  val FACE = Value("FACE")
  val NECK = Value("NECK")
  val UPPER_BODY = Value("UPPER_BODY")
  val WAIST = Value("WAIST")
  val LEFT_EAR = Value("LEFT_EAR")
  val RIGHT_EAR = Value("RIGHT_EAR")
  val LEFT_LEG = Value("LEFT_LEG")
  val RIGHT_LEG = Value("RIGHT_LEG")
  val LEFT_FOOT = Value("LEFT_FOOT")
  val RIGHT_FOOT = Value("RIGHT_FOOT")
}