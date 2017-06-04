package logic

import game.damage.Damage
import game.{Bullet, Creature, Thing, Tool}

object Combat {
  def apply(gameTime:Float, attacker:Thing,defender:Thing,weapon:Tool,bullet:Bullet):Unit = {

    var damage:Float = 0f
    for((k,dam) <- bullet.damages) {
      damage += dam.calculate(defender)
    }
    defender.damage(gameTime, damage)
  }
}
