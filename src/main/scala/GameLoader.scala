import com.badlogic.gdx.graphics.{Texture, OrthographicCamera}
import com.badlogic.gdx.graphics.g2d.{TextureRegion, BitmapFont, SpriteBatch}
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.maps.tiled.{TiledMapTile, TiledMapTileLayer, TmxMapLoader, TiledMap}
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.{BodyDef, Box2DDebugRenderer, World}

object GameLoader {
  lazy val batch: SpriteBatch = new SpriteBatch()
  lazy val camera: OrthographicCamera = new OrthographicCamera()
  lazy val world: World = new World(new Vector2(0, -100f), true)
  lazy val debugRenderer: Box2DDebugRenderer = new Box2DDebugRenderer()
  lazy val font: BitmapFont = new BitmapFont()

  lazy val levelMap:TiledMap = new TmxMapLoader().load("level01.tmx")
  lazy val level:Texture = new Texture("levels.png")
  lazy val levels = TextureRegion.split(level, 24, 24).head

  lazy val monsterSheet = new Texture("Humanoid0.png")
  lazy val monsters = TextureRegion.split(monsterSheet, 16, 16).head

  def createLevel(): Unit = {
    var levelMapRenderer:OrthogonalTiledMapRenderer = new OrthogonalTiledMapRenderer(levelMap)
    var tileX:Int = 48
    var tileY:Int = 48
    var tmtl = levelMap.getLayers.get("ground").asInstanceOf[TiledMapTileLayer]
    var x:Int = 0
    var y:Int = 0
    for( x <- 1 to 1000) {
      for(y <- 1 to 1000 ){
        var c = tmtl.getCell(x,y)
        if(c != null) {
          var posX:Int = x * tileX
          var posY:Int = y * tileY
          var t:TiledMapTile = c.getTile()
          new Thing(world, t.getTextureRegion(), BodyDef.BodyType.StaticBody, posX, posY)
        }
      }
    }
  }
}

