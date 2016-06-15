package tools

import game.{Thing, Creature, Bullet, Tool}


class Lantern extends Tool {
  this.attributes("brightness") = 2f
  this.attributes("luminance") = 1f
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
    bullet.attributes("brightness") = 2f
    bullet.attributes("luminance") = 1f
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

  override def mod(source:Thing, attribute:String, value:Float):Float = {
    if("luminance".equalsIgnoreCase(attribute)) {
      return 4.0f
    } else if("brightness".equalsIgnoreCase((attribute))) {
      return 4.0f
    } else {
      return super.mod(source, attribute, value)
    }

  }

  override def use(gameTime:Float, user:Creature):Boolean = {
    this.using = this.using == false
    return true
  }

  override def update(gameTime:Float): Unit = {
    super.update(gameTime)
  /*
    if(this.location != null) {
      if(this.using) {
        this.location.attributes("luminance") = 0.4f
        this.location.attributes("brightness") = (((this.location.attributes("brightness") * 2) +  3f + Math.random().toFloat) / 3)
      } else {
        this.location.attributes("luminance") = 0
        this.location.attributes("brightness") = 0
      }
    }
    */
    if(Math.random() * 10 > 8)
      this.attributes("brightness") = (((this.attributes("brightness") * 2) +  1f + Math.random().toFloat) / 3)
  }
}
