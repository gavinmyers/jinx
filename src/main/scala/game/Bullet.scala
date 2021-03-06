package game

import game.damage.Damage
import logic.Combat

class Bullet extends Thing {
  var damages:scala.collection.mutable.Map[String,Damage] = scala.collection.mutable.Map[String,Damage]()

  var cooldown:Float = 0.4f

  var forceX:Float = 0f
  var forceY:Float = 0f

  var created:Float = 0f
  var weapon:Tool = _
  var attacker:Creature = _
  var bind:Thing = _

  this.weight = 0.01f
  set(Attribute.V_FRICTION,new MaxCurrentMin(0f,0f,0f))
  set(Attribute.V_DENSITY,new MaxCurrentMin(0.1f,0.1f,0f))
  set(Attribute.V_RESTITUTION,new MaxCurrentMin(0f,0f,0f))
  set(Attribute.V_GRAVITY_SCALE,new MaxCurrentMin(0.1f,0.1f,0f))

  var speed:Float = 0f

  this.category = Thing.bullet
  var effect:Short = Bullet.explosion

  override def update(gameTime:Float): Unit = {
    if(created + cooldown < gameTime) {
      this.die()
    }
    if(this.bind != null) {
      if(faceH == "R") {
        this.transformX = this.bind.lastX + 18f
        this.transformY = this.bind.lastY
      } else {
        this.transformX = this.bind.lastX - 18f
        this.transformY = this.bind.lastY
      }
    }
  }

  override def contact(gameTime:Float, thing:Thing): Unit = {

    if(this.effect == Bullet.fire && !thing.destroyed && thing.get(Attribute.FLAMMABLE) != null) {
      this.created = gameTime
    }

    if(thing != attacker) {
      Combat.apply(gameTime, attacker, thing, weapon, this)
    }
  }


}

object Bullet {
  def slash: Short = 0x00

  def explosion: Short = 0x01

  def fire: Short = 0x02

  def catchem:Short = 0x03

}