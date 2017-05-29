package tools

import game.Tool

/**
  * Created by gavin on 5/26/17.
  */
class WoodColumn extends Tool {
  this.category = game.Thing.woodcolumn
  this.description = "a column of wood"
  this.attributes += "flammable" -> 1f
  this.wall = true
  this.weight = 1500f
}