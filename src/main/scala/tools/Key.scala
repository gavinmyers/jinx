package tools

import game.{Notification, Creature, Tool}
import logic.Messaging

class Key extends Tool {

  this.category = game.Thing.key
  this.scaleX = 0.5f
  this.scaleY = 0.5f

  override def use(gameTime:Float, user:Creature):Boolean = {

    for((k, thing) <- user.near) {
      if(thing.isInstanceOf[Tool]) {
        val tool:Tool = thing.asInstanceOf[Tool]
        if(tool.locked && tool.container) {
          tool.locked = false
          Messaging.send(this, thing, "Y", gameTime)
          this.die()
          return true
        } else {
          Messaging.send(this, thing, "N", gameTime)
        }
      }
    }
    return false
  }

}
