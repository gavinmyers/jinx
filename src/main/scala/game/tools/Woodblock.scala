package game.tools

import game.{Attribute, Tool}

/**
  * Created by gavin on 3/7/17.
  */
class Woodblock  extends Tool {
  this.category = game.Thing.woodblock
  this.description = "a block of wood"
  set(Attribute.FLAMMABLE,1f)
  this.wall = true
  this.weight = 500f
  this.platform = true
  this.set(Attribute.V_DENSITY,this.weight * 10f)
}
