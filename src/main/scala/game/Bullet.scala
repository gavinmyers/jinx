package game

import logic.Combat

class Bullet extends Thing {
  var cooldown:Float = 0.4f

  var forceX:Float = 0f
  var forceY:Float = 0f

  var created:Float = 0f
  var weapon:Tool = _
  var attacker:Creature = _
  var bind:Thing = _

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