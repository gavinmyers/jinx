package game.tools

import game.{Attribute, Tool}

class Cupcake extends Tool {
  this.category = game.Thing.cupcake
  set(Attribute.HUNGER, 2.0f)
  set(Attribute.FULLNESS, 100f)
  this.edible = true
}
