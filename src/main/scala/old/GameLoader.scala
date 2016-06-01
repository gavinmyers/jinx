package old

object GameLoader {
  def BOX_TO_WORLD = 32f
  var gameTime:Float = 0

  var goto:String = ""
  var target:String = ""

  var room:Room = _

  var player:Being = _
  var roomDb:scala.collection.mutable.Map[String,Room] = scala.collection.mutable.Map[String,Room]()

}

object GameUtil {

  def pixelsToMeters(v:Float):Float = {
    v / GameLoader.BOX_TO_WORLD
  }

  def metersToPixels(v:Float):Float = {
    v * GameLoader.BOX_TO_WORLD
  }
}
