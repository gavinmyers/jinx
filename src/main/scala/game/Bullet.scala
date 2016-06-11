package game

import logic.Combat

class Bullet extends Thing {
  var cooldown:Float = 0.4f
  var created:Float = 0f
  var weapon:Tool = _
  var attacker:Creature = _
  var bind:Thing = _

  var speed:Float = 0f

  this.category = Thing.bullet
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
