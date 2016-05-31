package generics

import scala.collection.mutable

class Room(
            var id: String = java.util.UUID.randomUUID.toString,
            var inventory: mutable.Map[String, Thing] = scala.collection.mutable.Map[String, Thing]())
  extends Thing {

  override def scaleX: Float = 1.0f
  override def scaleY: Float = 1.0f
  override def size: Float = 0f
  override def weight: Float = 0f
  override def category: Short = Thing.nothing
  override def description: String = "It's a room"
  override def posX: Float = 1.0f
  override def posY: Float = 1.0f
  override def height: Float = 100f
  override def width: Float = 100f
  override def location: Thing = null
  override def destroyed: Boolean = false


}
