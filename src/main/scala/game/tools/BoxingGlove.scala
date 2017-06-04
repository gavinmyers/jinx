package game.tools

import game.{BodyParts, Creature, Tool}

class BoxingGlove extends Tool {
  this.category = game.Thing.boxingglove
  bodyParts += BodyParts.LEFT_HAND
  bodyParts += BodyParts.RIGHT_HAND

  set("mod_strength",99)

  override def use(gameTime: Float, user: Creature): Boolean = {
    user.wear(gameTime, this)
    return true
  }
}
