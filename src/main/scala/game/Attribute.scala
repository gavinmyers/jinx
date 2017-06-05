package game

/**
  * Created by gavin on 6/4/17.
  */
object Attribute extends Enumeration with java.io.Serializable {
  type Attribute = Value
  val V_FRICTION = Value("V_FRICTION")
  val V_DENSITY = Value("V_DENSITY")
  val V_RESTITUTION = Value("V_RESTITUTION")
  val V_GRAVITY_SCALE = Value("V_GRAVITY_SCALE")
  val V_BRIGHTNESS = Value("V_BRIGHTNESS")
  val V_LUMINANCE = Value("V_LUMINANCE")

  val STRENGTH = Value("STRENGTH")
  val HEALTH = Value("HEALTH")
  val FULLNESS = Value("FULLNESS")
  val HUNGER = Value("HUNGER")

  val ENCUMBRANCE = Value("ENCUMBRANCE")

  val STUN = Value("STUN")


  val RUN_VELOCITY = Value("RUN_VELOCITY")
  val JUMP_VELOCITY = Value("JUMP_VELOCITY")
  val CLIMB_VELOCITY = Value("CLIMB_VELOCITY")
  val JUMP = Value("JUMP")


  val FLAMMABLE = Value("FLAMMABLE")


}
