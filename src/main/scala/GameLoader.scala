import box2dLight.{PositionalLight, PointLight, RayHandler}
import com.badlogic.gdx.graphics.{OrthographicCamera, Color, Texture}
import com.badlogic.gdx.graphics.g2d.{TextureRegion, BitmapFont, SpriteBatch}
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.maps.tiled.{TiledMapTile, TiledMapTileLayer, TmxMapLoader, TiledMap}
import com.badlogic.gdx.math.{Matrix4, Vector2}
import com.badlogic.gdx.physics.box2d.{JointEdge, BodyDef, Box2DDebugRenderer, World}
import scala.collection.JavaConversions._

import scala.collection.mutable.ListBuffer

object GameLoader {
  def BOX_TO_WORLD = 32f

  def create(level:String):Unit = {
    this.reset()

    levelMap = new TmxMapLoader().load(level + ".tmx")
    levelMapRenderer = new OrthogonalTiledMapRenderer(levelMap)

    GameLoader.camera.setToOrtho(false)
    GameLoader.parallalaxCameras.map(x => x.setToOrtho(false))

    GameLoader.levelMapRenderer.setView(GameLoader.camera)
    GameLoader.camera.translate(0, 0, 0)



  }

  def reset():Unit = {
    for (thing <- GameLoader.thingDb) {
      if(thing.light != null) {
        thing.light.remove()
      }
      for (je:JointEdge <- thing.body.getJointList) {
        GameLoader.world.destroyJoint(je.joint)
      }
      GameLoader.world.destroyBody(thing.body)
      GameLoader.thingDb -= thing
    }

    for (thing <- GameLoader.sceneryDb) {
      for (je:JointEdge <- thing.body.getJointList) {
        GameLoader.world.destroyJoint(je.joint)
      }
      GameLoader.world.destroyBody(thing.body)
      GameLoader.sceneryDb -= thing
    }


    this.monsterDb = scala.collection.mutable.Map[String,Being]()
    this.groundDb = scala.collection.mutable.Map[String,Brick]()
    this.thingDb = ListBuffer()
    this.effectDb = ListBuffer()
    this.bulletDb = ListBuffer()
    this.sceneryDb = ListBuffer()
  }

  var gameTime:Float = 0

  var goto:String = ""
  var target:String = ""

  lazy val batch: SpriteBatch = new SpriteBatch()
  lazy val camera: OrthographicCamera = new OrthographicCamera()
  lazy val parallalaxCameras:Array[OrthographicCamera] = Array.fill[OrthographicCamera](10)(new OrthographicCamera())

  lazy val world: World = new World(new Vector2(0, -75f), true)
  lazy val debugRenderer: Box2DDebugRenderer = new Box2DDebugRenderer()


  lazy val handler:RayHandler = new RayHandler(world)

  lazy val font: BitmapFont = new BitmapFont()

  var levelMap:TiledMap = _
  var levelMapRenderer:OrthogonalTiledMapRenderer = _


  var monsterDb:scala.collection.mutable.Map[String,Being] = scala.collection.mutable.Map[String,Being]()
  var groundDb:scala.collection.mutable.Map[String,Brick] = scala.collection.mutable.Map[String,Brick]()
  var thingDb:ListBuffer[Thing] = ListBuffer()
  var effectDb:ListBuffer[Effect] = ListBuffer()
  var bulletDb:ListBuffer[Bullet] = ListBuffer()
  var sceneryDb:ListBuffer[Scenery] = ListBuffer()

}

object GameUtil {

  def pixelsToMeters(v:Float):Float = {
    v / GameLoader.BOX_TO_WORLD
  }

  def metersToPixels(v:Float):Float = {
    v * GameLoader.BOX_TO_WORLD
  }
}
