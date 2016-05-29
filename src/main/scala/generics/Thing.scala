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

}


