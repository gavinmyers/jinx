package tools

import game.{Notification, Creature, Tool}

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
          thing.add({
            val n: Notification = new Notification
            n.message = "Y"
            n.startX = thing.startX
            n.startY = thing.startY + thing.height
            n.created = gameTime
            n
          })
          this.die()
          return true
        } else {
          thing.add({
            val n: Notification = new Notification
            n.message = "N"
            n.startX = thing.startX
            n.startY = thing.startY + thing.height
            n.created = gameTime
            n
          })
        }
      }
    }
    return false
  }

}
