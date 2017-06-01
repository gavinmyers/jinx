package game

/**
  * Created by gavin on 5/31/17.
  */
class MinMaxCurrent(var maximum : Float , var current : Float, var minimum: Float) {
  def this(current: Float) = this(0f,current,0f)
  def remaining = {
    maximum - current
  }
}