package game

import scala.collection.mutable

trait Tool extends Thing {
  def c:Float = 1
}

class GenericTool(
            var id: String = java.util.UUID.randomUUID.toString,
            var location: Thing = null,
            var destroyed: Boolean = false,
            var startX: Float = 1.0f,
            var startY: Float = 1.0f,
            var height: Float = 100f,
            var width: Float = 100f,
            var scaleX:Float = 1.0f,
            var scaleY:Float = 1.0f,
            var size:Float = 1f,
            var weight:Float = 10f,
            var category:Short = Thing.tool,
            var description:String = "It's a thing",
            var inventory: mutable.Map[String, Thing] = scala.collection.mutable.Map[String, Thing]())
  extends Tool {


}
