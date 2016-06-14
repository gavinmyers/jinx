package game

import ai.{GenericAI, AI}
import tools.{IronSword, Lantern}

import scala.collection.mutable

trait Thing {
  var id:String = java.util.UUID.randomUUID.toString
  var description:String = ""
  var ai:AI = _
  var location:Thing = _
  var inventory:scala.collection.mutable.Map[String,Thing] = scala.collection.mutable.Map[String, Thing]()
  var destroyed:Boolean = false
  var category:Short = Thing.nothing
  var healthMax: Float = 10f
  var healthCurrent: Float = 10f
  var movH: String = ""
  var faceH: String = ""
  var movV: String = ""
  var faceV: String = ""
  var takingDamage: Boolean = false
  var lastDamage: Float = -1.0f
  var damageCooldown: Float = 0.5f

  var lastUpdate:Float = 0
  var updateCooldown:Float = 1.0f

  var dieing: Boolean = false
  var deathStart: Float = 0
  var deathEnd: Float = 0.3f
  var scaleX:Float = 1.0f
  var scaleY:Float = 1.0f
  var startX:Float = 0.0f
  var startY:Float = 0.0f
  var height:Float = 12f
  var width:Float = 12f
  var size:Float = 1f
  var luminance:Float = 0f
  var brightness:Float = 0f
  var weight:Float = 1f
  var lastX:Float = 0
  var lastY:Float = 0
  var transformX:Float = 0
  var transformY:Float = 0
  def enter(thing:Thing):Unit = {
    if(thing.location != null) {
      thing.location.leave(thing)
    }
    inventory += thing.id -> thing
    thing.location = this
  }

  def leave(thing:Thing):Unit = {
    inventory -= thing.id
  }

  def contact(gameTime:Float, thing:Thing) : Unit = {

  }

  def update(gameTime:Float) : Unit = {
    this.takingDamage = lastDamage + damageCooldown > gameTime
    if(this.ai != null) {
      this.ai.think(gameTime, this)
    }
  }

  def damage(gameTime:Float, amount:Float) : Unit = {
    if(takingDamage) {
      return
    }
    println("OUCH " + amount)
    this.lastDamage = gameTime
    this.takingDamage = true
    this.healthCurrent -= amount
    if(this.healthCurrent < 1) {
      this.die()
    }
  }

  def die():Unit = {
    this.location.leave(this)
  }

}

object Thing {
  def nothing: Short = 0x00
  def floor: Short = 0x01
  def interaction:Short = 0x02
  def exit:Short = 0x05
  def entrance:Short = 0x06
  def ladder:Short = 0x07

  def bullet: Short = 0xB0

  def creature:Short = 0xC00
  def lilac: Short = 0xC01
  def snake: Short = 0xC02

  def tool: Short = 0xF00
  def lantern:Short = 0xF01
  def ironsword:Short = 0xF02
  def pigmask:Short = 0xF03
  def cupcake:Short = 0xF04
  def medicinewheel:Short = 0xF05

  def create(t:String):Thing = {
    if("lantern".equalsIgnoreCase(t)) {
      return new Lantern
    } else if("ironsword".equalsIgnoreCase(t)) {
      return new IronSword
    } else if("snake".equalsIgnoreCase(t)) {
      val gc:GenericCreature = new GenericCreature
      gc.category = Thing.snake
      gc.runMaxVelocity = 1f
      gc.healthMax = 10f
      gc.healthCurrent = 10f
      gc.holding = new IronSword
      gc.enter(gc.holding)
      gc.ai = new GenericAI
      return gc
    }

    return null
  }

}


