package game.tools

import game.{Attribute, Tool}

class Chest extends Tool {
  this.category = game.Thing.chest
  this.vanishOnDeath = false
  set(Attribute.FLAMMABLE,1f)
  this.description = "a box"
}
