package game

import scala.collection.mutable

class Room
  extends Thing {

  var title:String = "JINX: HP 0[0] | MP 0[0] | LVL 1 | EXP 0"
  var history:String = ""
  var menu:String = "[I] Inventory | [Z] Jump | [X] Attack | [P] Pickup Item | [D] Drop Item | [C] Use Item | [ESC] Pause | [E] Exit"
  var alert:String = ""
  var ambientLight:Float = 0.0f

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
