package game

/**
  * Created by gavin on 5/31/17.
  */
class MaxCurrentMin(var maximum : Float, var current : Float, var minimum: Float) extends java.io.Serializable {
  def this(current: Float) = this(0f,current,0f)
  def remaining = {
    maximum - current
  }
}