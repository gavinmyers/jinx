package game

import scala.collection.mutable

trait Tool extends Thing {
  var active:Boolean = false
  var cooldown:Float = 0.4f
  var lastUse:Float = 0f

  def use(gameTime:Float):Boolean = {
    this.active = this.active == false

    if(lastUse + cooldown > gameTime) {
      return false
    }

    lastUse = gameTime
    return true
  }
}
