package logic

import game.{Creature, Bullet, Tool, Thing}

object Combat {
  def apply(attacker:Thing,deffender:Thing,weapon:Tool,bullet:Bullet):Unit = {
    deffender.damage(2)
  }
}
