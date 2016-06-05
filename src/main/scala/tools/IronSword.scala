package tools

import game.{Thing, Creature, Bullet, Tool}

class IronSword extends Tool  {
  this.category = game.Thing.ironsword
  this.brightness = 0.75f
  this.luminance = 0.25f



  override def attack(gameTime:Float):Boolean = {
    if(!super.attack(gameTime)) {
      return false
    }

    val bullet:Bullet = new Bullet
    bullet.startX = this.location.lastX
    bullet.startY = this.location.lastY
    bullet.created = gameTime
    bullet.bind = this.location
    bullet.weapon = this
    if(this.location.isInstanceOf[Creature]) {
      val creature:Creature = this.location.asInstanceOf[Creature]
      bullet.attacker = creature
      bullet.movH = creature.movH
      bullet.faceH = creature.faceH

    }
    this.location.location.enter(bullet)

    return true
  }

  override def use(gameTime:Float):Boolean = {
    if(!super.use(gameTime)) {
      return false
    }


    return true
  }

}
