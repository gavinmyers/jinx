package game

import scala.collection.mutable

trait Creature extends Thing {


  attributes += "fullness_max" -> 600f
  attributes += "fullness_current" -> 600f
  attributes += "hunger" -> 1f

  var jump: Boolean = false
  var jumping: Boolean = false
  var lastJump: Float = 0

  attributes += "jump_max" -> 0.45f
  attributes += "jump_max_velocity" -> 15f
  attributes += "run_max_velocity" -> 5f

  var pickup:Boolean = false

  var canFly: Boolean = false

  var holding:Tool = _

  def use(gameTime:Float) = {
    if(holding != null) {
      holding.use(gameTime, this)
    }
  }

  def drop() = {
    if(holding != null) {
      val t:Thing = this.holding
      this.remove(t)
      t.startX = this.lastX
      t.startY = this.lastY
      t.lastX = this.lastX
      t.lastY = this.lastY
      this.location.add(t)
    }
  }

  def attack(gameTime:Float) = {
    if(holding != null) {
      holding.attack(gameTime)
    }
  }

  override def remove(thing:Thing):Unit = {
    super.remove(thing)
    if(this.holding == thing) {
      this.holding = null
    }
  }

  override def get(attribute:String):Float = {
    var mod:Float = super.get(attribute)
    if(this.holding != null) mod = this.holding.mod(this, attribute, mod)
    return mod
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

  override def contact(gameTime:Float, thing:Thing): Unit = {

    if(thing.isInstanceOf[Exit]) {
      val exit:Exit = thing.asInstanceOf[Exit]
      exit.destination.add(this)

      this.startX = exit.entrance.startX
      this.startY = exit.entrance.startY
      this.lastX = startX
      this.lastY = startY

    } else if(thing.isInstanceOf[Tool] && this.pickup) {
      val tool:Tool = thing.asInstanceOf[Tool]

      this.add(tool)
      this.pickup = false
      if(this.holding == null) {
        this.holding = tool
      }
    }


  }

  override def update(gameTime:Float) : Unit = {
    super.update(gameTime)
    if(lastUpdate + updateCooldown > gameTime) {
      return false
    }
    lastUpdate = gameTime

    var hungerCurrent = this.get("hunger")

    this.set("fullness_current", this.get("fullness_current") - hungerCurrent)
    if(this.get("fullness_current") < 1) {
      this.die()
    }

  }

}
