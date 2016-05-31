package generics

import scala.collection.mutable

class Tile(
  var id: String = java.util.UUID.randomUUID.toString,
  var location: Thing = null,
  var destroyed: Boolean = false,
  var posX: Float = 1.0f,
  var posY: Float = 1.0f,
  var height: Float = 100f,
  var width: Float = 100f,
  var inventory: mutable.Map[String, Thing] = scala.collection.mutable.Map[String, Thing]())
  extends Thing {

  override def scaleX: Float = 1.0f
  override def scaleY: Float = 1.0f
  override def size: Float = 0f
  override def weight: Float = 0f
  override def category: Short = Thing.floor
  override def description: String = "It's the floor"

}
