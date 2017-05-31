package game

import ai.{AI, GenericAI}
import logic.Messaging
import tools.{Woodblock, _}

import scala.collection.mutable

trait Thing {
  var id:String = java.util.UUID.randomUUID.toString
  var description:String = ""
  var ai:AI = _
  var friction:Float = 5f
  var density:Float = 4.0f
  var restitution:Float = 0f
  var gravityScale = 1f

  var location:Thing = _
  var inventory:scala.collection.mutable.Map[String,Thing] = scala.collection.mutable.Map[String, Thing]()
  var near:scala.collection.mutable.Map[(String, Float),Thing] = scala.collection.mutable.Map[(String, Float), Thing]()
  var attributes:scala.collection.mutable.Map[String,Float] = scala.collection.mutable.Map[String, Float]()
  var notifications:scala.collection.mutable.Map[String,Notification] = scala.collection.mutable.Map[String, Notification]()

  var destroyed:Boolean = false
  var category:Short = Thing.nothing

  attributes += "health_max" -> 34f
  attributes += "health_current" -> 34f

  var movH: String = ""
  var faceH: String = ""
  var movV: String = ""
  var faceV: String = ""
  var takingDamage: Boolean = false
  var lastDamage: Float = -1.0f
  var damageCooldown: Float = 0.5f

  var lastUpdate:Float = 0
  var updateCooldown:Float = 1.0f

  var vanishOnDeath:Boolean = true
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

  var weight:Float = 1f
  def absweight:Float = {
    var w = this.weight
    for((i,thing) <- this.inventory) {
      w += thing.absweight
    }
    return w
  }

  var lastX:Float = 0
  var lastY:Float = 0
  var transformX:Float = 0
  var transformY:Float = 0

  attributes += "brightness" -> 0
  attributes += "luminance" -> 0

  def mod(source:Thing, attribute:String, value:Float):Float = {
    if(this.attributes.contains("mod_"+attribute)) {
      return value + this.attributes("mod_"+attribute)
    } else {
      return value
    }
  }

  def set(attribute:String, value:Float) = {
    this.attributes(attribute) = value
  }

  def get(attribute:String):Float = {
    return this.attributes.get(attribute).get
  }

  def add(thing:Thing):Unit = {
    if(thing.location != null) {
      thing.location.remove(thing)
    }
    if(thing.isInstanceOf[Notification]) {
      notifications += thing.id -> thing.asInstanceOf[Notification]
    } else {
      inventory += thing.id -> thing
    }

    thing.location = this
  }

  def remove(thing:Thing):Unit = {
    if(thing.isInstanceOf[Notification]) {
      notifications -= thing.id
    } else {
      inventory -= thing.id
    }
  }

  def contact(gameTime:Float, thing:Thing) : Unit = {
    if(thing.category == Thing.floor || this.category == Thing.floor) {
      return
    }
    near += (thing.id,gameTime) -> thing
  }

  def cleanup(gameTime:Float): Unit = {
    if(this.category == Thing.floor) {
      return
    }
    for(((k,t), thing) <- near) {
      if(t != gameTime) {
        near.remove((k,t))
      }
    }
  }

  def update(gameTime:Float) : Unit = {
    this.takingDamage = lastDamage + damageCooldown > gameTime
    if(this.ai != null) {
      this.ai.think(gameTime, this)
    }
    for((k,t) <- this.inventory) {
      t.update(gameTime)
    }
  }

  def damage(gameTime:Float, amount:Float) : Unit = {
    this.takingDamage = lastDamage + damageCooldown > gameTime

    if(takingDamage) {
      return
    }

    this.lastDamage = gameTime
    this.takingDamage = true
    this.set("health_current", this.get("health_current") - amount)
    if(this.get("health_current") < 1) {
      this.die()
    }

    Messaging.send(this, this, "H" + (1 + ((this.get("health_current") / this.get("health_max")) * 10).toInt), gameTime)
  }

  def die():Unit = {
    this.destroyed = true
    if(vanishOnDeath == true) {
      this.location.remove(this)
    }
  }

}

trait NoDamage extends Thing {
  override def damage(gameTime:Float, amount:Float) : Unit = {
    return 0;
  }
}

trait NoDie extends Thing {
  override def die() : Unit = {
    return 0;
  }
}


object Thing {
  def nothing: Short = 0x00
  def floor: Short = 0x01
  def interaction:Short = 0x02
  def exit:Short = 0x05
  def entrance:Short = 0x06
  def ladder:Short = 0x07

  def bullet: Short = 0xB00

  def creature:Short = 0xC00
  def lilac: Short = 0xC01
  def snake: Short = 0xC02
  def phoenix: Short = 0xC03
  def spider: Short = 0xC04
  def skeletonwarrior: Short = 0xC05

  def woodblock:Short = 0xD00
  def woodcolumn:Short = 0xD01

  def tool: Short = 0xF00

  def lantern:Short = 0xF01
  def ironsword:Short = 0xF02
  def pigmask:Short = 0xF03
  def cupcake:Short = 0xF04
  def medicinewheel:Short = 0xF05
  def key:Short = 0xF06
  def corpse: Short = 0xF07
  def catchem:Short = 0xF08

  def chest:Short = 0xD00



  def notification:Short = 0xAAA

  def create(t:String):Thing = {
    if("lantern".equalsIgnoreCase(t)) {
      return new Lantern

    } else if("woodblock".equalsIgnoreCase(t)) {
      return new Woodblock

    } else if("woodcolumn".equalsIgnoreCase(t)) {
      return new WoodColumn

    } else if("ironsword".equalsIgnoreCase(t)) {
      return new IronSword

    } else if("fire".equalsIgnoreCase(t)) {
      val bullet:Bullet = new Bullet
      bullet.effect = Bullet.fire
      bullet.cooldown = Float.MaxValue
      bullet.attributes("brightness") = 2f
      bullet.attributes("luminance") = 1f
      return bullet


    } else if("catchem".equalsIgnoreCase(t)) {
      return new Catchem

    } else if("key".equalsIgnoreCase(t)) {
      return new Key

    } else if("chest".equalsIgnoreCase(t)) {
      val t:Tool = new Chest
      t.locked = true
      t.container = true
      t.add(Thing.create("lantern"))
      t.add(Thing.create("lantern"))
      t.add(Thing.create("lantern"))

      return t

    } else if("phoenix".equalsIgnoreCase(t)) {
      val gc:GenericCreature = new GenericCreature
      gc.category = Thing.phoenix
      gc.set("run_max_velocity", 1f)
      gc.set("jump_max_velocity", 2f)
      gc.set("health_current",10f)
      gc.set("health_max", 10f)
      gc.canFly = true
      gc.holding = new Firebreath
      gc.add(gc.holding)
      gc.weight = 0.01f
      gc.ai = new GenericAI
      return gc

    } else if("skeletonwarrior".equalsIgnoreCase(t)) {
      val gc:GenericCreature = new GenericCreature
      gc.category = Thing.skeletonwarrior
      gc.set("run_max_velocity", 1f)
      gc.set("health_current",10f)
      gc.set("health_max", 10f)
      gc.holding = new IronSword
      gc.add(gc.holding)
      gc.ai = new GenericAI
      return gc

    } else if("snake".equalsIgnoreCase(t)) {
      val gc:GenericCreature = new GenericCreature
      gc.category = Thing.snake
      gc.set("run_max_velocity", 1f)
      gc.set("health_current",10f)
      gc.set("health_max", 10f)
      gc.holding = new IronSword
      gc.add(gc.holding)
      gc.ai = new GenericAI
      return gc

    } else if("spider".equalsIgnoreCase(t)) {
      val gc:GenericCreature = new GenericCreature
      gc.category = Thing.spider
      gc.set("run_max_velocity", 1f)
      gc.set("health_current",10f)
      gc.set("health_max", 10f)
      gc.holding = new IronSword
      gc.add(gc.holding)
      gc.ai = new GenericAI
      return gc
    }

    return null
  }

}


