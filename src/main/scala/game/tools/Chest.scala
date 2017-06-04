package game.tools

import game.Tool

class Chest extends Tool {
  this.category = game.Thing.chest
  this.vanishOnDeath = false
  set("flammable",1f)
  this.description = "a box"
}
