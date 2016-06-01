package game

import scala.collection.mutable

class Entrance(
                var id: String = java.util.UUID.randomUUID.toString,
                var location: Thing,
                var destroyed: Boolean = false,
                var default:Boolean = false,
                var startX: Float = 1.0f,
                var startY: Float = 1.0f,
                var height: Float = 100f,
                var width: Float = 100f)
  extends Thing {

  override def scaleX: Float = 1.0f
  override def scaleY: Float = 1.0f
  override def size: Float = 0f
  override def weight: Float = 0f
  override def category: Short = Thing.entrance
  override def description: String = "It's the floor"
  override def inventory: mutable.Map[String, Thing] = scala.collection.mutable.Map[String, Thing]()

}
