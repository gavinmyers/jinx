package game.tools

import game.{Attribute, Tool}

/**
  * Created by gavin on 5/26/17.
  */
class WoodColumn extends Tool {
  this.category = game.Thing.woodcolumn
  this.description = "a column of wood"
  set(Attribute.FLAMMABLE,1f)
  this.wall = true
  this.platform = true
  this.weight = 500f
  this.set(Attribute.V_DENSITY,this.weight * 10f)

}
