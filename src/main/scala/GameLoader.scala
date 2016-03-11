import com.badlogic.gdx.graphics.{Texture, OrthographicCamera}
import com.badlogic.gdx.graphics.g2d.{TextureRegion, BitmapFont, SpriteBatch}
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.maps.tiled.{TiledMapTile, TiledMapTileLayer, TmxMapLoader, TiledMap}
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.{BodyDef, Box2DDebugRenderer, World}

object GameLoader {
  lazy val batch: SpriteBatch = new SpriteBatch()
  lazy val camera: OrthographicCamera = new OrthographicCamera()
  lazy val backgroundCamera: OrthographicCamera = new OrthographicCamera()
  lazy val world: World = new World(new Vector2(0, -100f), true)
  lazy val debugRenderer: Box2DDebugRenderer = new Box2DDebugRenderer()
  lazy val font: BitmapFont = new BitmapFont()

  lazy val levelMap:TiledMap = new TmxMapLoader().load("level01.tmx")
  lazy val level:Texture = new Texture("levels.png")
  lazy val levels = TextureRegion.split(level, 24, 24).head
  lazy val layer:TiledMapTileLayer = levelMap.getLayers.get("sky").asInstanceOf[TiledMapTileLayer]
  lazy val levelMapRenderer:OrthogonalTiledMapRenderer = new OrthogonalTiledMapRenderer(levelMap)

  lazy val playerSheet = new Texture("jayden.png")
  lazy val player = TextureRegion.split(playerSheet, 24, 24).head

  lazy val monsterSheet = new Texture("Humanoid0.png")
  lazy val monsters = TextureRegion.split(monsterSheet, 16, 16).head

  var monsterDb:scala.collection.mutable.Map[String,Thing] = scala.collection.mutable.Map[String,Thing]()
  var groundDb:scala.collection.mutable.Map[String,Thing] = scala.collection.mutable.Map[String,Thing]()
  var thingDb:List[Thing] = List()


  def drawThings(): Unit = {
    for(thing <- thingDb) {
      thing.draw(GameLoader.batch)
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
          groundDb += "ground_"+x+"_"+y ->  new Thing(world, t.getTextureRegion(), BodyDef.BodyType.StaticBody, posX, posY)
        }
      }
    }
  }
}

