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
  set("encumbrance", new MaxCurrentMin( get("strength").current * 10, 0f, 0f))
  set("jump", new MaxCurrentMin(0.45f, 0f, 0f))
  set("jump_velocity", new MaxCurrentMin(15f, 0f, -8f))
  set("run_velocity", new MaxCurrentMin(5f, 0f, -5f))
  set("climb_velocity", new MaxCurrentMin(5f, 0f, -5f))
  set("stun", new MaxCurrentMin(1.5f, 0f, 0f))

  var canJump:Boolean = false
  var canClimb:Boolean = false
  var isJumping:Boolean = false
  var isFalling:Boolean = false

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

  override def get(attribute:String):MaxCurrentMin = {
    var mmc:MaxCurrentMin = super.get(attribute)
    if(mmc == null)
      println(attribute + " not found")

    var mod:Float = mmc.current
    if(this.holding != null) mod = this.holding.mod(this, attribute, mod)
    return new MaxCurrentMin(mmc.maximum, mod, mmc.minimum)
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

  def runRampUp(): Unit = {
    val rv:MaxCurrentMin = get("run_velocity")

    if(movH == "L") {
      rv.current = rv.current - (rv.maximum * 0.1f)
      rv.current = Math.max(rv.current, rv.maximum * -1)
    }

    if(movH == "R") {
      rv.current = rv.current + (rv.maximum * 0.1f)
      rv.current = Math.min(rv.current, rv.maximum)
    }
    set("run_velocity", rv)
  }

  def runRampDown(): Unit = {
    val rv:MaxCurrentMin = get("run_velocity")
    rv.current = rv.current * 0.9f
    set("run_velocity", rv)
  }

  def doJump(): Unit = {
    if(!isFalling)
      isJumping = true
  }

  def stopJump(): Unit = {
    movV = ""
    isJumping = false
    isFalling = true
  }

  def updateJump(): Unit = {
    if(!isJumping) return

    val jv:MaxCurrentMin = get("jump_velocity")

    jv.current = jv.current + (jv.maximum * 0.1f)
    jv.current = Math.min(jv.current, jv.maximum)
    set("jump_velocity", jv)

    if(jv.current >= jv.maximum) {
      isJumping = false
      isFalling = true
    }
  }


  def updateFall(): Unit = {
    if(isJumping) return

    val jv:MaxCurrentMin = get("jump_velocity")
    if(canJump) {
      isFalling = false
      jv.current = jv.current * 0.9f
      set("jump_velocity", jv)
    } else {
      jv.current = jv.current + (jv.minimum * 0.1f)
      jv.current = Math.max(jv.current, jv.minimum)
      set("jump_velocity", jv)
    }
  }



  override def update(gameTime:Float) : Unit = {
    super.update(gameTime)
    if(movH == "R" || movH == "L") {
      runRampUp()
    } else {
      runRampDown()
    }

    set("gravityScale",get("gravityScale").maximum)

    if(canClimb) {
      set("gravityScale", 0f)
      val jv:MaxCurrentMin = get("climb_velocity")
      if(movV == "U") {
        set("jump_velocity", jv.maximum)
      } else if(movV == "D") {
        set("jump_velocity", jv.maximum * -1)
      } else {
        set("jump_velocity", 0f)
      }
    }  else if(movV == "U" && canJump && !isJumping) {
      doJump()
    } else if(isJumping && movV != "U") {
      stopJump()
    }
    if(!canClimb) {
      updateJump()
      updateFall()
    }

    if(lastUpdate + updateCooldown > gameTime) {
      return false
    }
    lastUpdate = gameTime

    var hungerCurrent:Float = this.get("hunger").current
    var mmc:MaxCurrentMin = this.get("fullness")

    set("fullness", mmc.current - hungerCurrent)

    if(this.get("fullness").current < 1) {
      this.die()
    }


  }

}
