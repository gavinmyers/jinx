package game.tools

import game._
import logic.Combat

class Catchem extends Tool {
  this.category = game.Thing.catchem
  this.scaleX = 0.8f
  this.scaleY = 0.8f
  this.description = "A small round ball"

  override def attack(gameTime:Float): Boolean = {
    if(!super.attack(gameTime)) {
      return false
    }

    if(this.inventory.isEmpty == false) {
      val creature:Creature = inventory.head._2.asInstanceOf[Creature]
      creature.startX = this.location.lastX
      creature.startY = this.location.lastY
      creature.ai.alignment = this.location.asInstanceOf[Creature].ai.alignment
      creature.ai.friends = this.location.asInstanceOf[Creature].ai.friends
      creature.ai.enemies = this.location.asInstanceOf[Creature].ai.enemies
      this.location.location.add(creature)
      this.die()
    } else {
      val bullet:CatchemBullet = new CatchemBullet
      bullet.catchem = this
      bullet.effect = Bullet.catchem
      bullet.startX = this.location.lastX
      bullet.startY = this.location.lastY
      bullet.created = gameTime
      bullet.cooldown = 3f
      bullet.weight = 0.1f
      bullet.forceY = 250f + (Math.random() * 50).toFloat

      bullet.set(Attribute.V_BRIGHTNESS, 2f)
      bullet.set(Attribute.V_LUMINANCE, 1f)
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
    }
    return true
  }

}

class CatchemBullet extends Bullet {
  var catchem:Catchem = _
  override def contact(gameTime:Float, thing:Thing): Unit = {
    if(thing.isInstanceOf[Creature] && thing != attacker && this.catchem.inventory.isEmpty) {
      catchem.startX = this.lastX
      catchem.lastX = this.lastX
      catchem.startY = this.lastY
      catchem.lastY = this.lastY
      catchem.add(thing)
      println(this.location + " vs " + catchem.startX + " vs " + this.lastX)
      this.location.add(catchem)
      this.die()
    }
  }
}
