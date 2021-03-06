package game.tools

import game.damage.{FireDamage, SharpDamage}
import game.{Attribute, Bullet, Creature, Tool}

class Firebreath extends Tool {
  this.category = game.Thing.ironsword

  override def attack(gameTime:Float):Boolean = {
    if(!super.attack(gameTime)) {
      return false
    }
    val bullet:Bullet = new Bullet
    val d:SharpDamage = new SharpDamage
    bullet.damages += d.id -> d
    val fd:FireDamage = new FireDamage
    bullet.damages += fd.id -> fd
    bullet.effect  = Bullet.fire
    bullet.startX = this.location.lastX
    bullet.startY = this.location.lastY
    bullet.created = gameTime
    bullet.cooldown = 3f
    bullet.weight = 0.02f
    bullet.forceY = 150f + (Math.random() * 50).toFloat

    bullet.set(Attribute.V_BRIGHTNESS,2f)
    bullet.set(Attribute.V_LUMINANCE,1f)
    bullet.weapon = this
    if(this.location.isInstanceOf[Creature]) {
      val creature:Creature = this.location.asInstanceOf[Creature]
      if(creature.faceH.equalsIgnoreCase("R")) {
        bullet.forceX = 350f + (Math.random() * 50).toFloat
      } else {
        bullet.forceX = (350f + (Math.random() * 50).toFloat) * -1
      }
      bullet.attacker = creature
      bullet.movH = creature.movH
      bullet.faceH = creature.faceH

    }
    this.location.location.add(bullet)

    return true
  }

  override def use(gameTime:Float, user:Creature):Boolean = {
    if(!super.use(gameTime, user)) {
      return false
    }
    return true
  }
}
