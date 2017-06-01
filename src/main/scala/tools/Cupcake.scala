package tools

import game.Tool

class Cupcake extends Tool {
  this.category = game.Thing.cupcake
  set("mod_hunger", 2.0f)
  set("fullness", 100f)
  this.edible = true
}
