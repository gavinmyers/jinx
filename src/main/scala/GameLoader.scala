import box2dLight.{PositionalLight, PointLight, RayHandler}
import com.badlogic.gdx.graphics.{Color, Texture, OrthographicCamera}
import com.badlogic.gdx.graphics.g2d.{TextureRegion, BitmapFont, SpriteBatch}
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.maps.tiled.{TiledMapTile, TiledMapTileLayer, TmxMapLoader, TiledMap}
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.{BodyDef, Box2DDebugRenderer, World}

import scala.collection.mutable.ListBuffer

object GameLoader {

  def create():Unit = {
    GameLoader.camera.setToOrtho(false)
    GameLoader.backgroundCamera.setToOrtho(false)
    GameLoader.levelMapRenderer.setView(GameLoader.camera)
    GameLoader.camera.translate(0, 0, 0)
  }

  var gameTime:Float = 0

  lazy val batch: SpriteBatch = new SpriteBatch()
  lazy val camera: OrthographicCamera = new OrthographicCamera()
  lazy val backgroundCamera: OrthographicCamera = new OrthographicCamera()
  lazy val world: World = new World(new Vector2(0, -100f), true)
  lazy val debugRenderer: Box2DDebugRenderer = new Box2DDebugRenderer()

  lazy val handler:RayHandler = new RayHandler(world)

  lazy val font: BitmapFont = new BitmapFont()

  lazy val levelMap:TiledMap = new TmxMapLoader().load("level01.tmx")
  lazy val level:Texture = new Texture("levels.png")
  lazy val levels = TextureRegion.split(level, 24, 24).head
  lazy val layer:TiledMapTileLayer = levelMap.getLayers.get("sky").asInstanceOf[TiledMapTileLayer]
  lazy val levelMapRenderer:OrthogonalTiledMapRenderer = new OrthogonalTiledMapRenderer(levelMap)

  lazy val playerSheet = new Texture("jayden.png")

  lazy val player:ListBuffer[TextureRegion] = ListBuffer()
  for(tr <- TextureRegion.split(playerSheet, 24, 24)) {
    for(tx <- tr) {
      player.append(tx)
    }
  }



  lazy val monsterSheet = new Texture("Humanoid0.png")
  lazy val monsters = TextureRegion.split(monsterSheet, 16, 16).head

  var monsterDb:scala.collection.mutable.Map[String,Being] = scala.collection.mutable.Map[String,Being]()
  var groundDb:scala.collection.mutable.Map[String,Brick] = scala.collection.mutable.Map[String,Brick]()
  var thingDb:ListBuffer[Thing] = ListBuffer()
  var bulletDb:ListBuffer[Bullet] = ListBuffer()

}

