package tools

import game.{Thing, Creature, Bullet, Tool}

class IronSword extends Tool  {
  this.category = game.Thing.ironsword
  this.description = "a sword"
  attributes += "mod_hunger" -> 2.0f


  override def attack(gameTime:Float):Boolean = {
    if(!super.attack(gameTime)) {
      return false
    }

    val bullet:Bullet = new Bullet
    bullet.effect = Bullet.slash
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
