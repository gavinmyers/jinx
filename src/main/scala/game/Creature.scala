package game


import game.Attribute.Attribute
import game.BodyPart.BodyPart
import logic.Messaging
import tools.Corpse
import scala.collection.mutable.ArrayBuffer



trait Creature extends Thing {

  var bodyParts:ArrayBuffer[BodyPart] = ArrayBuffer[BodyPart]()
  bodyParts += BodyPart.LEFT_HAND
  bodyParts += BodyPart.RIGHT_HAND

  var wearing:scala.collection.mutable.Map[BodyPart,Thing] = scala.collection.mutable.Map[BodyPart,Thing]()

  def wear(gameTime:Float, tool:Tool): Unit = {
    for(bp <- tool.bodyParts) {
      if(!this.bodyParts.contains(bp)) {
        println("Creatue does not have a " + bp + " to put this on")
        Messaging.send(this, tool,"N", gameTime)
        return
      }
    }

    for(bp <- tool.bodyParts) {
      this.wearing += bp -> tool
    }
    this.holding = null

  }

  set(Attribute.V_FRICTION,0.05f)
  set(Attribute.V_DENSITY,3.5f)



  set(Attribute.FULLNESS, 600f)
  set(Attribute.HUNGER, 1f)
  set(Attribute.STRENGTH, 12f)

  set(Attribute.ENCUMBRANCE, new MaxCurrentMin( get(Attribute.STRENGTH).current * 10, 0f, 0f))

  set(Attribute.JUMP, new MaxCurrentMin(0.45f, 0f, 0f))
  set(Attribute.JUMP_VELOCITY, new MaxCurrentMin(15f, 0f, -8f))
  set(Attribute.RUN_VELOCITY, new MaxCurrentMin(5f, 0f, -5f))
  set(Attribute.CLIMB_VELOCITY, new MaxCurrentMin(5f, 0f, -5f))
  set(Attribute.STUN, new MaxCurrentMin(1.5f, 0f, 0f))

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
        if(tool.absweight < get(Attribute.ENCUMBRANCE).remaining) {
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
    set(Attribute.ENCUMBRANCE, get(Attribute.ENCUMBRANCE).current + thing.absweight)
  }

  override def remove(thing:Thing):Unit = {
    if(!inventory.contains(thing.id) && !notifications.contains(thing.id)) return
    super.remove(thing)
    if(this.holding == thing) {
      this.holding = null
    }
    println("removing " + thing)
    set(Attribute.ENCUMBRANCE, get(Attribute.ENCUMBRANCE).current - thing.absweight)
  }

  override def get(attribute:Attribute):MaxCurrentMin = {
    var mmc:MaxCurrentMin = super.get(attribute)
    if(mmc == null) {
      println(attribute + " not found")
      return null
    }

    var mod:Float = mmc.current
    if(this.holding != null) mod = this.holding.mod(this, attribute, mod)
    for((bodyPart, tool) <- this.wearing) {
      mod = tool.mod(this, attribute, mod)
    }
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
    val rv:MaxCurrentMin = get(Attribute.RUN_VELOCITY)

    if(movH == "L") {
      rv.current = rv.current - (rv.maximum * 0.1f)
      rv.current = Math.max(rv.current, rv.maximum * -1)
    }

    if(movH == "R") {
      rv.current = rv.current + (rv.maximum * 0.1f)
      rv.current = Math.min(rv.current, rv.maximum)
    }
    set(Attribute.RUN_VELOCITY, rv)
  }

  def runRampDown(): Unit = {
    val rv:MaxCurrentMin = get(Attribute.RUN_VELOCITY)
    rv.current = rv.current * 0.9f
    set(Attribute.RUN_VELOCITY, rv)
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

    val jv:MaxCurrentMin = get(Attribute.JUMP_VELOCITY)

    jv.current = jv.current + (jv.maximum * 0.1f)
    jv.current = Math.min(jv.current, jv.maximum)
    set(Attribute.JUMP_VELOCITY, jv)

    if(jv.current >= jv.maximum) {
      isJumping = false
      isFalling = true
    }
  }


  def updateFall(): Unit = {
    if(isJumping) return

    val jv:MaxCurrentMin = get(Attribute.JUMP_VELOCITY)
    if(canJump) {
      isFalling = false
      jv.current = jv.current * 0.9f
      set(Attribute.JUMP_VELOCITY, jv)
    } else {
      jv.current = jv.current + (jv.minimum * 0.1f)
      jv.current = Math.max(jv.current, jv.minimum)
      set(Attribute.JUMP_VELOCITY, jv)
    }
  }



  override def update(gameTime:Float) : Unit = {
    super.update(gameTime)
    if(movH == "R" || movH == "L") {
      runRampUp()
    } else {
      runRampDown()
    }

    set(Attribute.V_GRAVITY_SCALE,get(Attribute.V_GRAVITY_SCALE).maximum)

    if(canClimb) {
      set(Attribute.V_GRAVITY_SCALE, 0f)
      val jv:MaxCurrentMin = get(Attribute.CLIMB_VELOCITY)
      if(movV == "U") {
        set(Attribute.JUMP_VELOCITY, jv.maximum)
      } else if(movV == "D") {
        set(Attribute.JUMP_VELOCITY, jv.maximum * -1)
      } else {
        set(Attribute.JUMP_VELOCITY, 0f)
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

    var hungerCurrent:Float = this.get(Attribute.HUNGER).current
    var mmc:MaxCurrentMin = this.get(Attribute.FULLNESS)

    set(Attribute.FULLNESS, mmc.current - hungerCurrent)

    if(this.get(Attribute.FULLNESS).current < 1) {
      this.die()
    }


  }

}
