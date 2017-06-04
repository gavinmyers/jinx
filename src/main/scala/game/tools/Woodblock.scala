package game.tools

import game.Tool

/**
  * Created by gavin on 3/7/17.
  */
class Woodblock  extends Tool {
  this.category = game.Thing.woodblock
  this.description = "a block of wood"
  set("flammable",1f)
  this.wall = true
  this.weight = 500f
  this.platform = true
  this.set("density",this.weight * 10f)
}