package game

import scala.collection.mutable

trait Thing {
  var id:String = java.util.UUID.randomUUID.toString
  var description:String = ""
  var location:Thing = _
  var inventory:scala.collection.mutable.Map[String,Thing] = scala.collection.mutable.Map[String, Thing]()
  var destroyed:Boolean = false
  var category:Short = Thing.nothing
  var scaleX:Float = 1.0f
  var scaleY:Float = 1.0f
  var startX:Float = 0.0f
  var startY:Float = 0.0f
  var height:Float = 12f
  var width:Float = 12f
  var size:Float = 1f
  var luminance:Float = 0f
  var brightness:Float = 0f
  var weight:Float = 1f
  var lastX:Float = 0
  var lastY:Float = 0
  var transformX:Float = 0
  var transformY:Float = 0
  def enter(thing:Thing):Unit = {
    if(thing.location != null) {
      thing.location.leave(thing)
    }
    inventory += thing.id -> thing
    thing.location = this
  }

  def leave(thing:Thing):Unit = {
    inventory -= thing.id
  }

  def contact(thing:Thing) : Unit = {

  }

  def update(gameTime:Float) : Unit = {

  }

}

object Thing {
  def nothing: Short = 0x00
  def floor: Short = 0x01
  def interaction:Short = 0x02
  def exit:Short = 0x05
  def entrance:Short = 0x06

  def bullet: Short = 0x04

  def creature:Short = 0xC00
  def lilac: Short = 0xC01

  def tool: Short = 0xF00
  def lantern:Short = 0xF01
  def ironsword:Short = 0xF02


}


