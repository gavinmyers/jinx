package display

import box2dLight.RayHandler
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.{GL20, OrthographicCamera}
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.maps.tiled.{TiledMapTile, TiledMapTileLayer, TmxMapLoader, TiledMap}
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.{World, Box2DDebugRenderer}
import generics.{Entity, Tile}
import utils.Conversion

object Scene {
  var gameTime:Float = 0
}

class Scene(map:String) {
  val tiles:TiledMap = new TmxMapLoader().load(map + ".tmx")
  val tileRenderer:OrthogonalTiledMapRenderer = new OrthogonalTiledMapRenderer(tiles)
  val camera:OrthographicCamera = new OrthographicCamera()
  val debugRenderer: Box2DDebugRenderer = new Box2DDebugRenderer()
  val world: World = new World(new Vector2(0, -75f), true)
  val handler:RayHandler = new RayHandler(world)
  val batch: SpriteBatch = new SpriteBatch()

  var features:scala.collection.mutable.Map[String,Feature] = {
    val ret:scala.collection.mutable.Map[String,Feature] = scala.collection.mutable.Map[String,Feature]()
    val tileX: Int = 24
    val tileY: Int = 24
    val tmtl = this.tiles.getLayers.get("ground").asInstanceOf[TiledMapTileLayer]
    for (x <- 0 to tmtl.getWidth) {
      for (y <- 0 to tmtl.getHeight) {
        val c = tmtl.getCell(x, y)
        if (c != null) {
          val px: Float = x * tileX + 12f
          val py: Float = y * tileY + 12f
          val t: TiledMapTile = c.getTile
          val ent:Entity = new Entity(posX=px, posY=py)
          val fet:Feature = new Feature(ent, world, t.getTextureRegion)
          ret += java.util.UUID.randomUUID.toString -> fet
        }
      }
    }
    ret
  }

  def render():Unit = {
    Gdx.gl.glClearColor(0, 0, 0, 1)
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
    batch.begin()
    Scene.gameTime += Gdx.graphics.getDeltaTime
    for ((k,v) <- features) {
      v.sprite.setPosition(Conversion.metersToPixels(v.body.getPosition.x) - v.sprite.getWidth/2 , Conversion.metersToPixels(v.body.getPosition.y) - v.sprite.getHeight/2 )
      v.sprite.draw(batch)
    }
    batch.end()
  }
}