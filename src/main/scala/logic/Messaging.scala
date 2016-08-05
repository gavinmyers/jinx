package logic

import game.{Thing, Notification}

object Messaging {

  def send(sender:Thing, target:Thing, message:String, gameTime:Float) = {
    target.add({
      val n: Notification = new Notification
      n.message = message
      n.startX = target.lastX
      n.startY = target.lastY + target.height
      n.created = gameTime
      n
    })
  }

}
