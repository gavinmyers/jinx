package game

import scala.collection.mutable

trait Creature extends Thing {


  var hungerMax: Float = 0f
  var hungerCurrent: Float = 0f

  var jump: Boolean = false
  var jumping: Boolean = false
  var jumpMax: Float = 0.45f
  var lastJump: Float = 0

  var attacking:Boolean = false


  var pickup:Boolean = false

  var canFly: Boolean = false

  var jumpMaxVelocity: Float = 15f
  var runMaxVelocity: Float = 2.5f

  var primary:Tool = _

  def use(gameTime:Float) = {
    if(primary != null) {
      primary.use(gameTime)
    }
  }

  def moveRight() = {
    movH = "R"
    faceH = "R"
  }

  def moveLeft() = {
    movH = "L"
    faceH = "L"
  }

  def moveUp() = {
    movV = "U"
  }

  def moveDown() = {
    movV = "D"
  }

  def stop() = {
    movH = ""
  }

}


class GenericCreature
  extends Creature {

  override def contact(thing:Thing): Unit = {

    if(thing.isInstanceOf[Exit]) {
      val exit:Exit = thing.asInstanceOf[Exit]
      exit.destination.enter(this)

      this.startX = exit.entrance.startX
      this.startY = exit.entrance.startY
      this.lastX = startX
      this.lastY = startY

    } else if(thing.isInstanceOf[Tool] && this.pickup) {
      val tool:Tool = thing.asInstanceOf[Tool]

      this.enter(tool)
      this.pickup = false
      if(this.primary == null) {
        this.primary = tool
      }
    }


  }

}
