package game

trait Thing {
  def id:String
  def description:String
  def location:Thing
  def inventory:scala.collection.mutable.Map[String,Thing]
  def destroyed:Boolean
  def category:Short
  def scaleX:Float
  def scaleY:Float
  def startX:Float
  def startY:Float
  def height:Float
  def width:Float
  def size:Float
  def weight:Float
  var lastX:Float = 0
  var lastY:Float = 0
  def enter(thing:Thing):Unit = {
    inventory += thing.id -> thing
  }

  def leave(thing:Thing):Unit = {
    inventory -= thing.id
  }

}

object Thing {
  def nothing: Short = 0x00
  def floor: Short = 0x01

  def exit:Short = 0x05
  def entrance:Short = 0x06

  def bullet: Short = 0x04

  def creature:Short = 0xC00
  def lilac: Short = 0xC01

  def tool: Short = 0xF00
  def lantern:Short = 0xF01


}


