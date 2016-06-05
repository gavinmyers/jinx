package game

import scala.collection.mutable

trait Tool extends Thing {
  var using:Boolean = false
  var useCooldown:Float = 0.4f
  var lastUse:Float = 0f

  var attacking:Boolean = false
  var attackCooldown:Float = 0.4f
  var lastAttack:Float = 0f

  override def update(gameTime:Float) : Unit = {
    super.update(gameTime)
    if(attackCooldown < 0f) {}
    else if(lastAttack + attackCooldown > gameTime) {
      this.attacking = true
    } else {
      this.attacking = false
    }
    if(useCooldown < 0f) {}
    else if(lastUse + useCooldown > gameTime) {
      this.using = true
    } else {
      this.using = false
    }
  }

  def attack(gameTime:Float):Boolean = {

    if(lastAttack + attackCooldown > gameTime) {
      return false
    }
    this.attacking = true
    lastAttack = gameTime
    return true
  }

  def use(gameTime:Float):Boolean = {

    if(lastUse + useCooldown > gameTime) {
      return false
    }

    this.using = true
    lastUse = gameTime
    return true
  }
}
