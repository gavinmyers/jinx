package tools

import game.{Creature, Bullet, Tool}


class Lantern extends Tool {
  this.brightness = 2
  this.luminance = 1f
  this.useCooldown = -1f
  this.category = game.Thing.lantern

  override def attack(gameTime:Float): Boolean = {
    if(!super.attack(gameTime)) {
      return false
    }

    val bullet:Bullet = new Bullet
    bullet.startX = this.location.lastX
    bullet.startY = this.location.lastY
    bullet.created = gameTime
    bullet.bind = this.location
    bullet.brightness = 2
    bullet.luminance = 1f
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


  override def use(gameTime:Float, user:Creature):Boolean = {
    this.using = this.using == false
    return true
  }

  override def update(gameTime:Float): Unit = {
    super.update(gameTime)

    if(this.location != null) {
      if(this.using) {
        this.location.luminance = 0.4f
        this.location.brightness = 3f + Math.random().toFloat
      } else {
        this.location.luminance = 0f
        this.location.brightness = 0f
      }
    }
    this.brightness = 2f + Math.random().toFloat
  }
}
