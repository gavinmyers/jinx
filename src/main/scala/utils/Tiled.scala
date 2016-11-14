package utils

import com.badlogic.gdx.maps.MapObject
import com.badlogic.gdx.maps.objects.RectangleMapObject
import com.badlogic.gdx.maps.tiled.{TiledMapTile, TiledMapTileLayer, TmxMapLoader, TiledMap}
import display.{VTile, VThing}
import game._
import tools.Lantern
import scala.collection.JavaConversions._
import scala.collection.mutable

object Tiled {

  var rooms:scala.collection.mutable.Map[String, Room] = scala.collection.mutable.Map[String, Room]()

  def load(map:String):Room = {

    if(rooms.containsKey(map)) {
      return rooms(map)
    }

    val r:Room = new Room
    r.id=map
    println(r.id + " vs "  + map)
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
          val tile:Tile = new Tile
          tile.startX=px
          tile.startY=py
          r.add(tile)
        }
      }
    }
    for (mo: MapObject <- tiles.getLayers.get("positions").getObjects) {
      def rct = mo.asInstanceOf[RectangleMapObject].getRectangle
      val px:Float = rct.x + 12
      val py:Float = rct.y + 12
      if ("thing".equalsIgnoreCase(mo.getName)) {
        val thing:Thing = Thing.create(mo.getProperties.get("type").toString)
        thing.startX = px
        thing.startY = py
        r.add(thing)
      }
      if ("ladder".equalsIgnoreCase(mo.getName)) {
        val ladder:Ladder = new Ladder
        ladder.startX = px
        ladder.startY = py
        ladder.height = rct.height
        ladder.width = rct.width
        r.add(ladder)
      }
      if ("target".equalsIgnoreCase(mo.getName)) {
        var ent:Entrance = new Entrance
        ent.id=mo.getProperties.get("id").toString
        ent.default=mo.getProperties.containsKey("default")
        ent.location = r
        ent.startX=px
        ent.startY=py
        ent.height = rct.height
        ent.width = rct.width
        r.add(ent)
      }
    }
    rooms += r.id -> r
    //calculate exits after building the room... otherwise you can get a deadlock
    for (mo: MapObject <- tiles.getLayers.get("positions").getObjects) {
      def rct = mo.asInstanceOf[RectangleMapObject].getRectangle
      val px:Float = rct.x + 12
      val py:Float = rct.y + 12
      if ("exit".equalsIgnoreCase(mo.getName)) {
        var destination:Room = load(mo.getProperties.get("goto").toString)
        var entrance:Thing = destination.inventory(mo.getProperties.get("target").toString)
        var exit:Exit = new Exit
        exit.location = r
        exit.destination = destination
        if(mo.getProperties.get("description") != null) {
          exit.description = mo.getProperties.get("description").toString
        } else {
          exit.description = "an exit is here"
        }
        exit.entrance = entrance
        exit.startX = px
        exit.startY = py
        exit.height = rct.height
        exit.width = rct.width
        r.add(exit)
      }
    }
    return r
  }

}
