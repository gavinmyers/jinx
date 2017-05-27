package tools

import game.Tool

class Chest extends Tool {
  this.category = game.Thing.chest
  this.vanishOnDeath = false
  this.attributes += "flammable" -> 1f
  this.description = "a box"
}
