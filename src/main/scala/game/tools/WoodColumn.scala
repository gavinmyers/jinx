package game.tools

import game.Tool

/**
  * Created by gavin on 5/26/17.
  */
class WoodColumn extends Tool {
  this.category = game.Thing.woodcolumn
  this.description = "a column of wood"
  set("flammable",1f)
  this.wall = true
  this.platform = true
  this.weight = 500f
  this.set("density",this.weight * 10f)

}
