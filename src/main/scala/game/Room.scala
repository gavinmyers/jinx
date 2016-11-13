package game

import scala.collection.mutable

class Room
  extends Thing {

  var title:String = "JINX: HP 0[0] | MP 0[0] | LVL 1 | EXP 0"
  var history:String = "> Something happened \n> Something else happened and it was a long event \n> Wow, things really keep happening\n> Something happened \n> Something else happened and it was a long event \n> Wow, things really keep happening\n"
  var menu:String = "[I] Inventory [Z] Jump [X] Attack"
  var alert:String = ""

  this.scaleX = 1.0f
  this.scaleY = 1.0f
  this.size = 0f
  this.weight = 0f
  this.category = Thing.nothing
  this.description = ""

  this.startX = 1.0f
  this.startY = 1.0f
  this.height = 100f
  this.width = 100f
  this.location = null
  this.destroyed = false


}
