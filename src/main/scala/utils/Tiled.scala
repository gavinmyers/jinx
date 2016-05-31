package utils

import com.badlogic.gdx.maps.MapObject
import com.badlogic.gdx.maps.objects.RectangleMapObject
import com.badlogic.gdx.maps.tiled.{TiledMapTile, TiledMapTileLayer, TmxMapLoader, TiledMap}
import display.{VTile, VThing}
import generics.{Entrance, Exit, Tile, Room}
import scala.collection.JavaConversions._

object Tiled {

  def mapToRoom(map:String):Room = {
    val r:Room = new Room()
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
          r.enter(new Tile(posX=px, posY=py))
        }
      }
    }
    for (mo: MapObject <- tiles.getLayers.get("positions").getObjects) {
      def rct = mo.asInstanceOf[RectangleMapObject].getRectangle
      if ("exit".equalsIgnoreCase(mo.getName)) {
        r.enter(new Exit(location = r, posX=rct.x+12f, posY=rct.y+12f, height = rct.height, width = rct.width))
      }
      if ("target".equalsIgnoreCase(mo.getName)) {
        r.enter(new Entrance(location = r, posX=rct.x+12f, posY=rct.y+12f, height = rct.height, width = rct.width))
      }
    }
    return r
  }

}
