package logic

import game.{Creature, Bullet, Tool, Thing}

object Combat {
  def apply(gameTime:Float, attacker:Thing,deffender:Thing,weapon:Tool,bullet:Bullet):Unit = {

    deffender.damage(gameTime, 2)
  }
}
