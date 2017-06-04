package game

import game.BodyParts.BodyParts
import logic.Messaging

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer


trait NoAttack extends Tool {
  override def attack(gameTime:Float):Boolean = {
    Messaging.send(this.location, this.location, "N", gameTime)
    return false
  }
}

trait NoUse extends Tool {
  override def use(gameTime:Float, user:Creature):Boolean = {
    Messaging.send(this.location, this.location, "N", gameTime)
    return false
  }
}



trait Tool extends Thing {

  var bodyParts:ArrayBuffer[BodyParts] = ArrayBuffer[BodyParts]()

  var using:Boolean = false
  var useCooldown:Float = 0.4f
  var lastUse:Float = 0f

  var attacking:Boolean = false
  var attackCooldown:Float = 0.4f
  var lastAttack:Float = 0f
  var wall = false

  var edible:Boolean = false
  var locked:Boolean = false
  var container:Boolean = false

  override def update(gameTime:Float) : Unit = {
    super.update(gameTime)
    if(attackCooldown < 0f) {}
    else if(lastAttack + attackCooldown > gameTime) {
      this.attacking = true
    } else {
      this.attacking = false
    }
    if(useCooldown < 0f) {}
    else if(lastUse + useCooldown > gameTime) {
      this.using = true
    } else {
      this.using = false
    }
  }

  def attack(gameTime:Float):Boolean = {

    if(lastAttack + attackCooldown > gameTime) {
      return false
    }
    this.attacking = true
    lastAttack = gameTime
    return true
  }

  def use(gameTime:Float, user:Creature):Boolean = {

    if(this.edible) {
      user.set("fullness", user.get("fullness").current + this.get("fullness").current)
      this.die()
    }

    if(lastUse + useCooldown > gameTime) {
      Messaging.send(user, this, "N", gameTime)
      return false
    }

    this.using = true
    lastUse = gameTime
    return true
  }
}
