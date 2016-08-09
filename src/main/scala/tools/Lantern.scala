package tools

import game.{Thing, Creature, Bullet, Tool}


class Lantern extends Tool {
  attributes += "brightness" -> 2.0f
  attributes += "luminance" -> 1.0f

  this.weight = 0.1f
  this.useCooldown = -1f
  this.category = game.Thing.lantern

  override def attack(gameTime:Float): Boolean = {
    if(!super.attack(gameTime)) {
      return false
    }

    this.damage(gameTime, (this.get("health_max") * 0.2).toInt)
    val bullet:Bullet = new Bullet
    bullet.effect = Bullet.fire
    bullet.startX = this.location.lastX
    bullet.startY = this.location.lastY
    bullet.created = gameTime
    bullet.cooldown = 3f
    bullet.weight = 0.1f
    bullet.forceY = 150f + (Math.random() * 50).toFloat

    bullet.attributes("brightness") = 2f
    bullet.attributes("luminance") = 1f
    bullet.weapon = this
    if(this.location.isInstanceOf[Creature]) {
      val creature:Creature = this.location.asInstanceOf[Creature]
      if(creature.faceH.equalsIgnoreCase("R")) {
        bullet.forceX = 150f + (Math.random() * 50).toFloat
      } else {
        bullet.forceX = (150f + (Math.random() * 50).toFloat) * -1
      }
      bullet.attacker = creature
      bullet.movH = creature.movH
      bullet.faceH = creature.faceH

    }
    this.location.location.add(bullet)

    return true
  }

  override def mod(source:Thing, attribute:String, value:Float):Float = {
    if(this.using == false) {
      return super.mod(source, attribute, value)
    } else if("luminance".equalsIgnoreCase(attribute)) {
      return 4.0f * (this.get("health_current") / this.get("health_max"))
    } else if("brightness".equalsIgnoreCase(attribute)) {
      return 4.0f * (this.get("health_current") / this.get("health_max"))
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
    if(Math.random() * 10 > 4)
      this.attributes("brightness") = (((this.attributes("brightness") * 2) +  1f + Math.random().toFloat) / 3)
  }
}
