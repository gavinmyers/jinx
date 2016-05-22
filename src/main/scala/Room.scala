import box2dLight.{PointLight, RayHandler}
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.{Color, OrthographicCamera}
import com.badlogic.gdx.graphics.g2d.{BitmapFont, SpriteBatch, Sprite}
import com.badlogic.gdx.maps.MapObject
import com.badlogic.gdx.maps.objects.RectangleMapObject
import com.badlogic.gdx.maps.tiled.{TiledMapTile, TiledMapTileLayer, TmxMapLoader, TiledMap}
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.math.{Matrix4, Vector3, Vector2}
import com.badlogic.gdx.physics.box2d._
import scala.collection.JavaConversions._

import scala.collection.mutable.ListBuffer

class Room(name:String) {
  var gameTime:Float = 0

  lazy val batch: SpriteBatch = new SpriteBatch()
  lazy val camera: OrthographicCamera = new OrthographicCamera()
  lazy val guiCamera: OrthographicCamera = new OrthographicCamera()
  lazy val parallalaxCameras:Array[OrthographicCamera] = Array.fill[OrthographicCamera](10)(new OrthographicCamera())


  lazy val debugRenderer: Box2DDebugRenderer = new Box2DDebugRenderer()

  lazy val world: World = new World(new Vector2(0, -75f), true)
  lazy val handler:RayHandler = new RayHandler(world)

  var levelMap:TiledMap = _
  var levelMapRenderer:OrthogonalTiledMapRenderer = _


  var monsterDb:scala.collection.mutable.Map[String,Being] = scala.collection.mutable.Map[String,Being]()
  var groundDb:scala.collection.mutable.Map[String,Brick] = scala.collection.mutable.Map[String,Brick]()
  var thingDb:ListBuffer[Thing] = ListBuffer()
  var effectDb:ListBuffer[Effect] = ListBuffer()
  var toolDb:ListBuffer[Tool] = ListBuffer()
  var bulletDb:ListBuffer[Bullet] = ListBuffer()
  var sceneryDb:ListBuffer[Scenery] = ListBuffer()

  var startX:Float = 0f
  var startY:Float = 0f

  GameLoader.roomDb += name -> this

  def start(target:String): Unit = {
    for (mo: MapObject <- levelMap.getLayers.get("positions").getObjects) {
      if ("target".equalsIgnoreCase(mo.getName)) {
        if (target.equalsIgnoreCase(mo.getProperties.get("id").toString)) {
          def r = mo.asInstanceOf[RectangleMapObject].getRectangle
          this.startX = r.x
          this.startY = r.y
        }
      }
    }
  }

  def init(target:String):Unit = {
    this.levelMap = new TmxMapLoader().load(name + ".tmx")
    this.levelMapRenderer = new OrthogonalTiledMapRenderer(levelMap)

    this.camera.setToOrtho(false)
    this.parallalaxCameras.map(x => x.setToOrtho(false))

    this.levelMapRenderer.setView(this.camera)
    this.camera.translate(0, 0, 0)

    this.guiCamera.setToOrtho(false)
    this.guiCamera.translate(0,0,0)

    this.handler.setAmbientLight(0.2f, 0.2f, 0.2f, 0.6f)
    start(target)
    drawLayer("ground")
    drawLadder("ladder")

    for (mo: MapObject <- levelMap.getLayers.get("positions").getObjects) {
      if ("target".equalsIgnoreCase(mo.getName)) {
        new Phoenix("phoenix", this, startX, startY, 1.0f, 1.0f)
      }
      if ("exit".equalsIgnoreCase(mo.getName)) {
        def r = mo.asInstanceOf[RectangleMapObject].getRectangle
        new Exit("exit", mo.getProperties.get("goto").toString, mo.getProperties.get("target").toString, this, r.x + 12 , r.y + 12 , r.width, r.height)
      }
      if ("scenery_ember".equalsIgnoreCase(mo.getName)) {
        def r = mo.asInstanceOf[RectangleMapObject].getRectangle
        for (i <- 0.to(r.width.toInt) by 24) {
          new Embers("ember", this, r.x + i + 12 , r.y + 12 )
        }
      }
      if ("scenery_fire".equalsIgnoreCase(mo.getName)) {
        def r = mo.asInstanceOf[RectangleMapObject].getRectangle
        for (i <- 0.to(r.width.toInt) by 24) {
          new Flames("fire", this, r.x + i + 12 , r.y + 12 )
        }
      }
    }
  }


  def drawLayer(name: String): Unit = {
    val tileX: Int = 24
    val tileY: Int = 24
    val tmtl = this.levelMap.getLayers.get(name).asInstanceOf[TiledMapTileLayer]
    for (x <- 0 to tmtl.getWidth) {
      for (y <- 0 to tmtl.getHeight) {
        val c = tmtl.getCell(x, y)
        if (c != null) {
          val posX: Int = x * tileX + 12
          val posY: Int = y * tileY + 12
          val t: TiledMapTile = c.getTile
          new Brick("ground_" + x + "_" + y, this, t.getTextureRegion, posX , posY )
        }
      }
    }
  }


  def drawLadder(name: String): Unit = {
    val tileX: Int = 24
    val tileY: Int = 24
    val tmtl = this.levelMap.getLayers.get(name).asInstanceOf[TiledMapTileLayer]
    for (x <- 0 to tmtl.getWidth) {
      for (y <- 0 to tmtl.getHeight) {
        val c = tmtl.getCell(x, y)
        if (c != null) {
          val posX: Int = x * tileX + 12
          val posY: Int = y * tileY + 12
          val t: TiledMapTile = c.getTile
          new Ladder("ladder" + x + "_" + y, this, t.getTextureRegion, BodyDef.BodyType.StaticBody, posX , posY )
        }
      }
    }
  }


  def draw(debug:Boolean):Unit = {
    if (GameLoader.player == null) {
      return
    }
    def player = GameLoader.player
    val position: Vector3 = camera.position
    val backgroundPallalaxTmp: Array[Vector3] = Array.fill[Vector3](10)(new Vector3())

    position.x += (player.sprite.getX - position.x) * 99f * Gdx.graphics.getDeltaTime
    position.y = startY

    backgroundPallalaxTmp(0).y = position.y
    backgroundPallalaxTmp(0).x = startX
    var spd = 0.1f
    for (i <- 1 to 9) {
      backgroundPallalaxTmp(i).x = startX - ((startX - player.sprite.getX) * spd)
      backgroundPallalaxTmp(i).y = position.y
      spd += 0.1f
    }

    camera.position.set(position)

    camera.zoom = 0.5f
    parallalaxCameras.foreach(f => f.zoom = 0.5f)

    camera.update()
    parallalaxCameras.foreach(f => f.update())

    world.step(Gdx.graphics.getDeltaTime, 6, 2)

    for (thing <- thingDb) {
      if (thing.destroyed) {
        for (je: JointEdge <- thing.body.getJointList) {
          world.destroyJoint(je.joint)
        }
        world.destroyBody(thing.body)
        thingDb -= thing
      }

    }

    for (i <- 0 to 9) {
      parallalaxCameras(i).position.set(backgroundPallalaxTmp(i))
      batch.setProjectionMatrix(parallalaxCameras(i).combined)
      batch.begin()
      //GameLoader.levelMapRenderer.renderTileLayer(GameLoader.levelMap.getLayers().get("ladder").asInstanceOf[TiledMapTileLayer])
      def camera = parallalaxCameras(i)
      def x: Float = camera.position.x - camera.viewportWidth * camera.zoom
      def y: Float = camera.position.y - camera.viewportHeight * camera.zoom
      def width: Float = camera.viewportWidth * camera.zoom * 2
      def height: Float = camera.viewportHeight * camera.zoom * 4
      levelMapRenderer.setView(camera.combined, x, y, width, height)
      levelMapRenderer.render(Array(i))

      batch.end()
    }

    handler.setCombinedMatrix(camera)
    batch.setProjectionMatrix(camera.combined)
    batch.begin()

    for (thing <- sceneryDb) {
      if (!thing.destroyed) {
        thing.move(GameLoader.gameTime)
        thing.draw(batch)
      }
    }

    for (thing <- thingDb) {
      if (!thing.destroyed) {
        thing.move(GameLoader.gameTime)
        thing.draw(batch)
      }
    }
    batch.end()

    batch.begin()
    //GameLoader.levelMapRenderer.renderTileLayer(GameLoader.levelMap.getLayers().get("ladder").asInstanceOf[TiledMapTileLayer])
    levelMapRenderer.setView(camera)
    levelMapRenderer.render(Array(13, 14, 15, 16, 17, 18, 19, 20, 21, 22))
    batch.end()

    def debugMatrix: Matrix4 = batch.getProjectionMatrix.cpy().scale(GameLoader.BOX_TO_WORLD, GameLoader.BOX_TO_WORLD, 0f)
    handler.setCombinedMatrix(debugMatrix)
    handler.updateAndRender()

    if (debug)
      GameLoader.room.debugRenderer.render(GameLoader.room.world, debugMatrix)
  }

}
