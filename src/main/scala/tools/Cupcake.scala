package tools

import game.Tool

class Cupcake extends Tool {
  this.category = game.Thing.cupcake
  attributes += "mod_hunger" -> 2.0f
  attributes += "fullness" -> 100f
  this.edible = true
}
