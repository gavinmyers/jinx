package tools

import game.{Creature, Bullet, Tool}

class Catchem extends Tool {
  this.category = game.Thing.catchem
  this.scaleX = 0.8f
  this.scaleY = 0.8f

  override def attack(gameTime:Float): Boolean = {
    if(!super.attack(gameTime)) {
      return false
    }


    val bullet:Bullet = new Bullet
    bullet.effect = Bullet.catchem
    bullet.startX = this.location.lastX
    bullet.startY = this.location.lastY
    bullet.created = gameTime
    bullet.cooldown = 3f
    bullet.weight = 0.1f
    bullet.forceY = 250f + (Math.random() * 50).toFloat

    bullet.attributes("brightness") = 2f
    bullet.attributes("luminance") = 1f
    bullet.weapon = this
    if(this.location.isInstanceOf[Creature]) {
      val creature:Creature = this.location.asInstanceOf[Creature]
      if(creature.faceH.equalsIgnoreCase("R")) {
        bullet.forceX = 250f + (Math.random() * 50).toFloat
      } else {
        bullet.forceX = (250f + (Math.random() * 50).toFloat) * -1
      }
      bullet.attacker = creature
      bullet.movH = creature.movH
      bullet.faceH = creature.faceH

    }
    this.location.location.add(bullet)
    bullet.add(this)

    return true
  }

}
