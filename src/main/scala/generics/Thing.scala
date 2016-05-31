package generics

trait Thing {
  def id:String
  def description:String
  def location:Thing
  def inventory:scala.collection.mutable.Map[String,Thing]
  def destroyed:Boolean
  def category:Short
  def scaleX:Float
  def scaleY:Float
  def posX:Float
  def posY:Float
  def height:Float
  def width:Float
  def size:Float
  def weight:Float

  def enter(thing:Thing):Unit = {
    inventory += thing.id -> thing
  }

  def leave(thing:Thing):Unit = {
    inventory -= thing.id
  }

}

object Thing {
  def nothing: Short = 0x1234
  def floor: Short = 0x0001
  def creature: Short = 0x0002
  def item: Short = 0x0003
  def bullet: Short = 0x004
}


