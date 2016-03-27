import box2dLight.RayHandler
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.{Texture, Color, GL20}
import com.badlogic.gdx.maps.objects.RectangleMapObject
import com.badlogic.gdx.maps.tiled.{TiledMapTile, TiledMapTileLayer}
import com.badlogic.gdx.math.{Vector3, Matrix4, Rectangle, Vector2}
import com.badlogic.gdx.physics.box2d._
import com.badlogic.gdx.{InputProcessor, Input, ApplicationAdapter, Gdx}
import scala.collection.JavaConversions._
import scala.collection.mutable.ListBuffer

class Sinx extends ApplicationAdapter with InputProcessor {
  println("Sinx")

  RayHandler.setGammaCorrection(true)
  RayHandler.useDiffuseLight(true)

  override def create(): Unit = {
    GameLoader.create()
    GameLoader.handler.setAmbientLight(0.2f, 0.2f, 0.2f, 0.6f)
    Gdx.input.setInputProcessor(this)

    drawLayer("ground")
    //fix
    drawLadder("ladder")

    def r = GameLoader.levelMap
      .getLayers
      .get("positions")
      .getObjects
      .get("player_start")
      .asInstanceOf[RectangleMapObject]
      .getRectangle

    def playerSheet = new Texture("jayden.png")
    val player:ListBuffer[TextureRegion] = ListBuffer()
    for(tr <- TextureRegion.split(playerSheet, 24, 24)) {
      for(tx <- tr) {
        player.append(tx)
      }
    }

    def bulletSheet = new Texture("bullet1.png")
    val bullet:ListBuffer[TextureRegion] = ListBuffer()
    for(tr <- TextureRegion.split(bulletSheet, 24, 24)) {
      for(tx <- tr) {
        bullet.append(tx)
      }
    }

    var p = new Being("player",GameLoader.world, player, r.x, r.y)
    p.bulletSheet = bullet

    var m = new Being("monster",GameLoader.world, player, r.x + 24, r.y + 24)
    m.bulletSheet = bullet
  }

  var debug:Boolean = false
  override def render(): Unit = {
    def player = GameLoader.monsterDb("player")


    Gdx.gl.glClearColor(0, 0, 0, 1)
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

    def lerp:Float = 4.1f
    val position:Vector3 = GameLoader.camera.position;
    position.x += (player.sprite.getX - position.x) * lerp * Gdx.graphics.getDeltaTime()
    position.y += (player.sprite.getY - position.y) * lerp * Gdx.graphics.getDeltaTime()

    GameLoader.camera.position.set(position)
    GameLoader.backgroundCamera.position.set(position)

    GameLoader.camera.zoom = 1f
    GameLoader.backgroundCamera.zoom = 1f

    GameLoader.camera.update()
    GameLoader.backgroundCamera.update()
    GameLoader.handler.setCombinedMatrix(GameLoader.camera)
    GameLoader.batch.setProjectionMatrix(GameLoader.backgroundCamera.combined)

    GameLoader.gameTime += Gdx.graphics.getDeltaTime()
    GameLoader.world.step(Gdx.graphics.getDeltaTime(), 6, 2)


    if(GameLoader.monsterDb.containsKey("monster")) {
      def monster = GameLoader.monsterDb("monster")
      monster.moveLeft(GameLoader.gameTime)
    }


    GameLoader.batch.begin()
    //GameLoader.levelMapRenderer.renderTileLayer(GameLoader.levelMap.getLayers().get("ladder").asInstanceOf[TiledMapTileLayer])
    GameLoader.levelMapRenderer.setView(GameLoader.backgroundCamera)
    GameLoader.levelMapRenderer.render(Array(0,1,2))
    GameLoader.batch.end()



    GameLoader.batch.begin()
    GameLoader.font.draw(GameLoader.batch, "Hello World", 500, 500)
    drawThings()
    GameLoader.batch.end()

    def debugMatrix:Matrix4 = GameLoader.batch.getProjectionMatrix().cpy().scale(GameLoader.BOX_TO_WORLD, GameLoader.BOX_TO_WORLD, 0f)
    GameLoader.handler.setCombinedMatrix(debugMatrix)
    GameLoader.handler.updateAndRender()

    for(contact <- GameLoader.world.getContactList) {
      if(contact.getFixtureA == null || contact.getFixtureA.getUserData == null || contact.getFixtureB == null || contact.getFixtureB.getUserData == null) {
      } else if(contact.getFixtureA.getUserData.isInstanceOf[Thing] && contact.getFixtureB.getUserData.isInstanceOf[Thing]) {
        //a(b)
        contact.getFixtureA.getUserData.asInstanceOf[Thing].contact(contact.getFixtureB.getUserData.asInstanceOf[Thing])
        //b(a)
        contact.getFixtureB.getUserData.asInstanceOf[Thing].contact(contact.getFixtureA.getUserData.asInstanceOf[Thing])
      }
    }

    //GameLoader.monsterDb("monster").handler.updateAndRender()

    if(Gdx.input.isKeyJustPressed(Input.Keys.D))
      debug = debug == false

    if(debug)
      GameLoader.debugRenderer.render(GameLoader.world, debugMatrix)

  }


  override def dispose(): Unit = {
    println("dispose")
  }


  def drawThings(): Unit = {


    for(thing <- GameLoader.thingDb) {
      thing.update(GameLoader.batch)
      thing.draw(GameLoader.batch)
    }
  }

  def drawLayer(name:String): Unit = {
    val tileX:Int = 24
    val tileY:Int = 24
    val tmtl = GameLoader.levelMap.getLayers.get(name).asInstanceOf[TiledMapTileLayer]
    var x:Int = 0
    var y:Int = 0

    for( x <- 0 to tmtl.getWidth) {
      for(y <- 0 to tmtl.getHeight ){
        val c = tmtl.getCell(x,y)
        if(c != null) {
          val posX:Int = x * tileX + 12
          val posY:Int = y * tileY + 12
          val t:TiledMapTile = c.getTile()
          new Brick("ground_"+x+"_"+y, GameLoader.world, t.getTextureRegion(), posX, posY)
        }
      }
    }
  }


  def drawLadder(name:String): Unit = {
    val tileX:Int = 24
    val tileY:Int = 24
    val tmtl = GameLoader.levelMap.getLayers.get(name).asInstanceOf[TiledMapTileLayer]
    var x:Int = 0
    var y:Int = 0

    for( x <- 0 to tmtl.getWidth) {
      for(y <- 0 to tmtl.getHeight ){
        val c = tmtl.getCell(x,y)
        if(c != null) {
          val posX:Int = x * tileX + 12
          val posY:Int = y * tileY + 12
          val t:TiledMapTile = c.getTile()
          new Ladder("ladder"+x+"_"+y, GameLoader.world, t.getTextureRegion(), BodyDef.BodyType.StaticBody, posX, posY)
        }
      }
    }
  }


  var downButtons:Float = 0
  override def keyDown(keycode: Int): Boolean = {

    def player = GameLoader.monsterDb("player")

    if(Input.Keys.LEFT == keycode)
      player.moveLeft(GameLoader.gameTime)

    if(Input.Keys.RIGHT == keycode)
      player.moveRight(GameLoader.gameTime)

    if(Input.Keys.UP == keycode)
      player.moveUp(GameLoader.gameTime)

    if(Input.Keys.DOWN == keycode)
      player.moveDown(GameLoader.gameTime)

    if(Input.Keys.Z == keycode)
      player.jump(GameLoader.gameTime)

    if(Input.Keys.X == keycode)
      player.attack(GameLoader.gameTime)

    return true
  }

  override def keyUp(keycode: Int): Boolean = {
    def player = GameLoader.monsterDb("player")

    if((Input.Keys.LEFT == keycode && Gdx.input.isKeyPressed(Input.Keys.RIGHT) == false) || (Input.Keys.RIGHT == keycode && Gdx.input.isKeyPressed(Input.Keys.LEFT) == false))
      player.stop(GameLoader.gameTime)

    if(Input.Keys.UP == keycode)
      player.fall(GameLoader.gameTime)

    if(Input.Keys.SPACE == keycode)
      player.attack(GameLoader.gameTime)

    return true
  }

  override def mouseMoved(screenX: Int, screenY: Int): Boolean = {
    return true
  }

  override def keyTyped(character: Char): Boolean = {
    return true
  }


  override def touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean = {
    return true
  }

  override def scrolled(amount: Int): Boolean = {
    return true
  }

  override def touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean = {
    return true
  }

  override def touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean = {
    return true
  }
}

