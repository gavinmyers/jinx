import box2dLight.{PositionalLight, PointLight, RayHandler}
import com.badlogic.gdx.graphics.{Color, Texture, OrthographicCamera}
import com.badlogic.gdx.graphics.g2d.{TextureRegion, BitmapFont, SpriteBatch}
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.maps.tiled.{TiledMapTile, TiledMapTileLayer, TmxMapLoader, TiledMap}
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.{BodyDef, Box2DDebugRenderer, World}

import scala.collection.mutable.ListBuffer

object GameLoader {

  var gameTime:Float = 0

  lazy val batch: SpriteBatch = new SpriteBatch()
  lazy val camera: OrthographicCamera = new OrthographicCamera()
  lazy val backgroundCamera: OrthographicCamera = new OrthographicCamera()
  lazy val world: World = new World(new Vector2(0, -100f), true)
  lazy val debugRenderer: Box2DDebugRenderer = new Box2DDebugRenderer()

  lazy val handler:RayHandler = new RayHandler(world)
  lazy val light:PositionalLight = new PointLight(handler, 1024, new Color(1f, 1f, 1f, 0.8f), 256, 0, 0);

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


  def drawThings(): Unit = {
    for(bullet <- bulletDb) {

      val vel:Vector2 = bullet.body.getLinearVelocity
      if(bullet.direction.equalsIgnoreCase("R")) {
        vel.x += 50
      } else {
        vel.x -= 50
      }
      bullet.body.setLinearVelocity(vel)
      if(bullet.life + bullet.created < GameLoader.gameTime) {
        bullet.destroy()
      }
    }
    for(thing <- thingDb) {
      thing.update(batch)
      thing.draw(batch)
    }
  }

  def createLevel(): Unit = {
    val tileX:Int = 24
    val tileY:Int = 24
    val tmtl = levelMap.getLayers.get("ground").asInstanceOf[TiledMapTileLayer]
    var x:Int = 0
    var y:Int = 0
    for( x <- -1000 to 1000) {
      for(y <- -1000 to 1000 ){
        val c = tmtl.getCell(x,y)
        if(c != null) {
          val posX:Int = x * tileX + 12
          val posY:Int = y * tileY + 12
          val t:TiledMapTile = c.getTile()
          groundDb += "ground_"+x+"_"+y ->  new Brick(world, t.getTextureRegion(), BodyDef.BodyType.StaticBody, posX, posY)
        }
      }
    }
  }
}

