package tools

import game.{Creature, Bullet, Tool}

class IronSword extends Tool  {
  this.category = game.Thing.ironsword
  this.brightness = 0.75f
  this.luminance = 0.25f

  override def update(gameTime:Float): Unit = {
    if(this.location.isInstanceOf[Creature]) {
      val creature:Creature = this.location.asInstanceOf[Creature]
      if(creature.attacking && lastUse + cooldown < gameTime) {
        creature.attacking = false
      }
    }

  }

  override def use(gameTime:Float):Boolean = {
    if(!super.use(gameTime)) {
      return false
    }

    val bullet:Bullet = new Bullet
    bullet.startX = this.location.lastX
    bullet.startY = this.location.lastY
    bullet.created = gameTime
    bullet.bind = this.location
    if(this.location.isInstanceOf[Creature]) {
      val creature:Creature = this.location.asInstanceOf[Creature]
      creature.attacking = true
      bullet.movH = creature.movH
      bullet.faceH = creature.faceH

    }
    this.location.location.enter(bullet)



    return true
  }
}
