package game

import logic.Messaging
import tools.Corpse

import scala.collection.mutable

trait Creature extends Thing {


  set("friction",0.05f)
  set("density",3.5f)

  set("fullness", 600f)
  set("hunger", 1f)
  set("strength", 12f)
  set("encumbrance", new MinMaxCurrent( get("strength").current * 10, 0f, 0f))
  set("jump", new MinMaxCurrent(0.45f, 0f, 0f))
  set("jump_velocity", new MinMaxCurrent(15f, 0f, 0f))
  set("run_velocity", new MinMaxCurrent(5f, 0f, 0f))

  var jump: Boolean = false
  var lastJump: Float = 0

  def exit(gameTime:Float) = {

    for((k,thing) <- this.near) {
      if(thing.isInstanceOf[Exit]) {
        val exit:Exit = thing.asInstanceOf[Exit]
        exit.destination.add(this)
        this.startX = exit.entrance.startX
        this.startY = exit.entrance.startY
        this.lastX = startX
        this.lastY = startY
      }
    }
  }

  def pickup(gameTime:Float) = {
    for((k,thing) <- this.near) {
      if(thing.isInstanceOf[Tool]) {
        val tool:Tool = thing.asInstanceOf[Tool]
        if(tool.absweight < get("encumbrance").remaining) {
          this.add(tool)

          if(this.holding == null) {
            this.holding = tool
          }
        } else {
          Messaging.send(this, thing,"W",gameTime)
        }

      }
    }
  }
  def open(gameTime:Float):Thing = {
    for ((k, thing) <- this.near) {
      if (thing.isInstanceOf[Tool]) {
        val t: Tool = thing.asInstanceOf[Tool]
        if (t.container == false) {
          Messaging.send(this, t, "N", gameTime)

        } else if (t.locked == true) {
          Messaging.send(this, t, "L", gameTime)

        } else if (t.locked == false) {
          Messaging.send(this, t, "Y", gameTime)
          return t
        }
      }
    }
    return null
  }

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

  override def add(thing:Thing):Unit = {
    super.add(thing)
    println("adding " + thing)
    set("encumbrance", get("encumbrance").current + thing.absweight)
  }

  override def remove(thing:Thing):Unit = {
    if(!inventory.contains(thing.id) && !notifications.contains(thing.id)) return
    super.remove(thing)
    if(this.holding == thing) {
      this.holding = null
    }
    println("removing " + thing)
    set("encumbrance", get("encumbrance").current - thing.absweight)
  }

  override def get(attribute:String):MinMaxCurrent = {
    var mmc:MinMaxCurrent = super.get(attribute)
    if(mmc == null)
      println(attribute + " not found")

    var mod:Float = mmc.current
    if(this.holding != null) mod = this.holding.mod(this, attribute, mod)
    return new MinMaxCurrent(mmc.maximum, mod, mmc.minimum)
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
    super.contact(gameTime, thing)
  }

  override def die():Unit = {
    this.location.add({val n:Corpse = new Corpse
      n.startX = this.lastX
      n.startY = this.lastY
      n})
    super.die()
  }

  override def update(gameTime:Float) : Unit = {
    super.update(gameTime)
    if(lastUpdate + updateCooldown > gameTime) {
      return false
    }
    lastUpdate = gameTime

    var hungerCurrent:Float = this.get("hunger").current
    var mmc:MinMaxCurrent = this.get("fullness")

    set("fullness", mmc.current - hungerCurrent)

    if(this.get("fullness").current < 1) {
      this.die()
    }

  }

}
