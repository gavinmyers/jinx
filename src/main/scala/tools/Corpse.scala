package tools

import game.Tool

class Corpse extends Tool {
  this.category = game.Thing.corpse
  this.vanishOnDeath = false
  this.description = "a pile of bones"
}
