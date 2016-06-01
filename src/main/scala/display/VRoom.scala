package display

import box2dLight.RayHandler
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.{GL20, OrthographicCamera}
import com.badlogic.gdx.maps.MapObject
import com.badlogic.gdx.maps.objects.RectangleMapObject
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.maps.tiled.{TiledMapTile, TiledMapTileLayer, TmxMapLoader, TiledMap}
import com.badlogic.gdx.math.{Matrix4, Vector3, Vector2}
import com.badlogic.gdx.physics.box2d.{World, Box2DDebugRenderer}
import game.{Thing, Room, Tile}
import utils.Conversion
import scala.collection.JavaConversions._

object VRoom {
  var gameTime:Float = 0
}

class VRoom(map:String, room:Room) {
  val tiles:TiledMap = new TmxMapLoader().load(map + ".tmx")
  val tileRenderer:OrthogonalTiledMapRenderer = new OrthogonalTiledMapRenderer(tiles)

  val camera:OrthographicCamera = new OrthographicCamera()
  val parallalaxCameras:Array[OrthographicCamera] = Array.fill[OrthographicCamera](10)(new OrthographicCamera())

  val debugRenderer: Box2DDebugRenderer = new Box2DDebugRenderer()
  val world: World = new World(new Vector2(0, -75f), true)

  val handler:RayHandler = new RayHandler(world)
  this.handler.setAmbientLight(0.6f, 0.6f, 0.6f, 0.4f)

  val batch: SpriteBatch = new SpriteBatch()

  val cameraStart: Vector3 = {
    val res:Vector3 = new Vector3()
    for (mo: MapObject <- tiles.getLayers.get("positions").getObjects) {
      if ("target".equalsIgnoreCase(mo.getName)) {
        if (mo.getProperties.get("default") != null) {
          def r = mo.asInstanceOf[RectangleMapObject].getRectangle
          res.x = r.x
          res.y = r.y
        }
      }
    }
    res
  }

  var vinventory:scala.collection.mutable.Map[String,VThing] = {
    val ret:scala.collection.mutable.Map[String,VThing] = scala.collection.mutable.Map[String,VThing]()
    for((k,thing) <- room.inventory) {
      ret += thing.id -> VThing.create(thing, world)
    }
    ret
  }


  def render(targetX:Float, targetY:Float):Unit = {

    Gdx.gl.glClearColor(0, 0, 0, 1)
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

    camera.zoom = 0.5f
    camera.setToOrtho(false)
    camera.translate(0, 0, 0)
    camera.position.set(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, 0)
    val position: Vector3 = new Vector3(cameraStart.x, cameraStart.y, cameraStart.z)
    //position.x = cameraStart.x
    position.x = targetX
    camera.position.set(position)
    camera.update()

    var spd = 0.1f
    val backgroundPallalaxTmp: Array[Vector3] = Array.fill[Vector3](10)(new Vector3())
    for (i <- 0 to 9) {
      backgroundPallalaxTmp(i).x = cameraStart.x - ((cameraStart.x - targetX) * spd)
      //backgroundPallalaxTmp(i).x = targetX - (targetX * spd)
      backgroundPallalaxTmp(i).y = cameraStart.y
      spd += 0.1f
    }


    world.step(Gdx.graphics.getDeltaTime, 6, 2)

    for (i <- 0 to 9) {
      val cam:OrthographicCamera = parallalaxCameras(i)
      cam.zoom = 0.5f
      cam.setToOrtho(false)
      cam.translate(0,0,0)
      cam.position.set(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, 0)
      cam.position.set(backgroundPallalaxTmp(i))
      cam.update()

      batch.setProjectionMatrix(cam.combined)
      batch.begin()
      def x: Float = cam.position.x - cam.viewportWidth * cam.zoom
      def y: Float = cam.position.y - cam.viewportHeight * cam.zoom
      def width: Float = cam.viewportWidth * cam.zoom * 2
      def height: Float = cam.viewportHeight * cam.zoom * 4
      tileRenderer.setView(cam.combined, x, y, width, height)
      tileRenderer.render(Array(i))
      batch.end()
    }

    handler.setCombinedMatrix(camera)
    batch.setProjectionMatrix(camera.combined)
    batch.begin()
    VRoom.gameTime += Gdx.graphics.getDeltaTime
    for((k,thing) <- room.inventory) {
      if(vinventory.contains(k) == false) {
        vinventory += thing.id -> VThing.create(thing, world)
      }
      val fet:VThing = vinventory(k)
      fet.update(VRoom.gameTime)
      fet.sprite.setPosition(Conversion.metersToPixels(fet.body.getPosition.x) - fet.sprite.getWidth/2 , Conversion.metersToPixels(fet.body.getPosition.y) - fet.sprite.getHeight/2 )
      fet.sprite.draw(batch)
    }

    batch.end()

    batch.begin()
    tileRenderer.setView(camera)
    tileRenderer.render(Array(13, 14, 15, 16, 17, 18, 19, 20, 21, 22))
    batch.end()


    handler.updateAndRender()

    def debugMatrix: Matrix4 = batch.getProjectionMatrix.cpy().scale(Conversion.BOX_TO_WORLD, Conversion.BOX_TO_WORLD, 0f)
    handler.setCombinedMatrix(debugMatrix)
    debugRenderer.render(world, debugMatrix)

  }
}