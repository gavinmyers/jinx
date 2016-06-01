package utils

import com.badlogic.gdx.maps.MapObject
import com.badlogic.gdx.maps.objects.RectangleMapObject
import com.badlogic.gdx.maps.tiled.{TiledMapTile, TiledMapTileLayer, TmxMapLoader, TiledMap}
import display.{VTile, VThing}
import game.{Entrance, Exit, Tile, Room}
import scala.collection.JavaConversions._

object Tiled {

  def mapToRoom(map:String):Room = {
    val r:Room = new Room(id=map)
    val tiles:TiledMap = new TmxMapLoader().load(map + ".tmx")
    val w: Int = 24
    val h: Int = 24
    val tmtl = tiles.getLayers.get("ground").asInstanceOf[TiledMapTileLayer]
    for (x <- 0 to tmtl.getWidth) {
      for (y <- 0 to tmtl.getHeight) {
        val c = tmtl.getCell(x, y)
        if (c != null) {
          val px: Float = x * w + 12f
          val py: Float = y * h + 12f
          val t: TiledMapTile = c.getTile
          r.enter(new Tile(startX=px, startY=py))
        }
      }
    }
    for (mo: MapObject <- tiles.getLayers.get("positions").getObjects) {
      def rct = mo.asInstanceOf[RectangleMapObject].getRectangle
      val px:Float = rct.x + 12
      val py:Float = rct.y + 12
      if ("exit".equalsIgnoreCase(mo.getName)) {
        r.enter(new Exit(location = r, destination=mo.getProperties.get("goto").toString, entrance=mo.getProperties.get("target").toString, startX=px, startY=py, height = rct.height, width = rct.width))
      }
      if ("target".equalsIgnoreCase(mo.getName)) {
        r.enter(new Entrance(id=mo.getProperties.get("id").toString, default=mo.getProperties.containsKey("default"), location = r, startX=px, startY=py, height = rct.height, width = rct.width))
      }
    }
    return r
  }

}
