package game.tools

import game.{Attribute, BodyPart, Creature, Tool}

class BoxingGlove extends Tool {
  this.category = game.Thing.boxingglove
  bodyParts += BodyPart.LEFT_HAND
  bodyParts += BodyPart.RIGHT_HAND

  modset(Attribute.STRENGTH,99)

  override def use(gameTime: Float, user: Creature): Boolean = {
    user.wear(gameTime, this)
    return true
  }
}
