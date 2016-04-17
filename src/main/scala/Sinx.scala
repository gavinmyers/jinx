import box2dLight.{PointLight, RayHandler}
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


    {
      def r = GameLoader.levelMap
        .getLayers
        .get("positions")
        .getObjects
        .get("player_start")
        .asInstanceOf[RectangleMapObject]
        .getRectangle

      var p = new Lilac("player", GameLoader.world, r.x, r.y, 1.0f, 1.0f)
      p.light = new PointLight(GameLoader.handler, 128, new Color(1f, 1f, 1f, 0.8f), 24, 0, 0)
      p.light.attachToBody(p.body, 0, 0)
      p.light.setIgnoreAttachedBody(true)
      p.light.setContactFilter(0, 2, -1)
      p.brain = null
      p.runMaxVelocity = 5f


      for (x <- 0 to 1) {
        var mod: Float = (((Math.random() * 125) + 20) / 100).toFloat
        var m = new Chick("monster", GameLoader.world, r.x + 248, r.y, 1.0f * mod, 1.0f * mod)
      }


      for (x <- 0 to 1) {
        var mod: Float = (((Math.random() * 25) + 100) / 100).toFloat
        var m = new Icebird("monster", GameLoader.world, r.x + 248, r.y, 1.0f * mod, 1.0f * mod)
      }

      for (x <- 0 to 1) {
        var mod: Float = (((Math.random() * 25) + 100) / 100).toFloat
        var m = new Firefox("monster", GameLoader.world, r.x + 248, r.y, 1.0f * mod, 1.0f * mod)
      }

      for (x <- 0 to 1) {
        var mod: Float = (((Math.random() * 125) + 20) / 100).toFloat
        var m = new Snake("monster", GameLoader.world, r.x + 248, r.y, 1.0f * mod, 1.0f * mod)
      }

      for (x <- 0 to 1) {
        var mod: Float = (((Math.random() * 125) + 40) / 100).toFloat
        var m = new Cockatrice("monster", GameLoader.world, r.x + 248, r.y, 1.0f * mod, 1.0f * mod)
      }

      for (x <- 0 to 1) {
        var mod: Float = (((Math.random() * 125) + 60) / 100).toFloat
        var m = new Phoenix("monster", GameLoader.world, r.x + 248, r.y, 1.0f * mod, 1.0f * mod)
      }

      for (x <- 0 to 1) {
        var mod: Float = (((Math.random() * 125) + 80) / 100).toFloat
        var m = new Zombie("monster", GameLoader.world, r.x + 248, r.y, 1.0f * mod, 1.0f * mod)
      }

    }

  }

  var debug: Boolean = false

  override def render(): Unit = {
    for (thing <- GameLoader.thingDb) {
      if (!thing.destroyed) {
        thing.update(GameLoader.gameTime)
      }
    }

    Gdx.gl.glClearColor(0, 0, 0, 1)
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

    def lerp: Float = 4.1f
    val position: Vector3 = GameLoader.camera.position

    if (GameLoader.monsterDb.contains("player")) {
      def player = GameLoader.monsterDb("player")
      position.x += (player.sprite.getX - position.x) * lerp * Gdx.graphics.getDeltaTime
      position.y += (player.sprite.getY - position.y) * lerp * Gdx.graphics.getDeltaTime
    }


    GameLoader.camera.position.set(position)
    GameLoader.backgroundCamera.position.set(position)

    GameLoader.camera.zoom = 0.5f
    GameLoader.backgroundCamera.zoom = 0.5f

    GameLoader.camera.update()
    GameLoader.backgroundCamera.update()
    GameLoader.handler.setCombinedMatrix(GameLoader.camera)
    GameLoader.batch.setProjectionMatrix(GameLoader.backgroundCamera.combined)

    GameLoader.gameTime += Gdx.graphics.getDeltaTime

    GameLoader.world.step(Gdx.graphics.getDeltaTime, 6, 2)
    for (thing <- GameLoader.thingDb) {
      if (thing.destroyed) {
        for (je: JointEdge <- thing.body.getJointList) {
          GameLoader.world.destroyJoint(je.joint)
        }
        GameLoader.world.destroyBody(thing.body)
        GameLoader.thingDb -= thing
      }

    }



    GameLoader.batch.begin()
    //GameLoader.levelMapRenderer.renderTileLayer(GameLoader.levelMap.getLayers().get("ladder").asInstanceOf[TiledMapTileLayer])
    GameLoader.levelMapRenderer.setView(GameLoader.backgroundCamera)
    GameLoader.levelMapRenderer.render(Array(0, 1, 2, 3, 4, 5))
    GameLoader.batch.end()



    GameLoader.batch.begin()
    GameLoader.font.draw(GameLoader.batch, "Hello World", 500, 500)
    for (thing <- GameLoader.thingDb) {
      if (!thing.destroyed) {
        thing.move(GameLoader.gameTime)
        thing.draw(GameLoader.batch)
      }
    }
    GameLoader.batch.end()

    GameLoader.batch.begin()
    //GameLoader.levelMapRenderer.renderTileLayer(GameLoader.levelMap.getLayers().get("ladder").asInstanceOf[TiledMapTileLayer])
    GameLoader.levelMapRenderer.setView(GameLoader.backgroundCamera)
    GameLoader.levelMapRenderer.render(Array(6, 7, 8))
    GameLoader.batch.end()

    def debugMatrix: Matrix4 = GameLoader.batch.getProjectionMatrix.cpy().scale(GameLoader.BOX_TO_WORLD, GameLoader.BOX_TO_WORLD, 0f)
    GameLoader.handler.setCombinedMatrix(debugMatrix)
    GameLoader.handler.updateAndRender()

    for (c <- GameLoader.world.getContactList) {

      if (c.getFixtureA == null || c.getFixtureA.getUserData == null || c.getFixtureB == null || c.getFixtureB.getUserData == null) {
      } else c.getFixtureA.getUserData match {
        case thing: Thing if c.getFixtureB.getUserData.isInstanceOf[Thing] =>
          //a(b)
          thing.contact(c.getFixtureB.getUserData.asInstanceOf[Thing])
          //b(a)
          c.getFixtureB.getUserData.asInstanceOf[Thing].contact(thing)
        case _ =>
      }
    }

    //GameLoader.monsterDb("monster").handler.updateAndRender()


    if (Gdx.input.isKeyJustPressed(Input.Keys.D))
      debug = !debug

    if (debug)
      GameLoader.debugRenderer.render(GameLoader.world, debugMatrix)

  }


  override def dispose(): Unit = {
    println("dispose")
  }

  def drawLayer(name: String): Unit = {
    val tileX: Int = 24
    val tileY: Int = 24
    val tmtl = GameLoader.levelMap.getLayers.get(name).asInstanceOf[TiledMapTileLayer]
    for (x <- 0 to tmtl.getWidth) {
      for (y <- 0 to tmtl.getHeight) {
        val c = tmtl.getCell(x, y)
        if (c != null) {
          val posX: Int = x * tileX + 12
          val posY: Int = y * tileY + 12
          val t: TiledMapTile = c.getTile
          new Brick("ground_" + x + "_" + y, GameLoader.world, t.getTextureRegion, posX, posY)
        }
      }
    }
  }


  def drawLadder(name: String): Unit = {
    val tileX: Int = 24
    val tileY: Int = 24
    val tmtl = GameLoader.levelMap.getLayers.get(name).asInstanceOf[TiledMapTileLayer]
    for (x <- 0 to tmtl.getWidth) {
      for (y <- 0 to tmtl.getHeight) {
        val c = tmtl.getCell(x, y)
        if (c != null) {
          val posX: Int = x * tileX + 12
          val posY: Int = y * tileY + 12
          val t: TiledMapTile = c.getTile
          new Ladder("ladder" + x + "_" + y, GameLoader.world, t.getTextureRegion, BodyDef.BodyType.StaticBody, posX, posY)
        }
      }
    }
  }


  var downButtons: Float = 0

  override def keyDown(keycode: Int): Boolean = {
    if (!GameLoader.monsterDb.contains("player")) return false

    def player = GameLoader.monsterDb("player")

    if (Input.Keys.LEFT == keycode)
      player.moveLeft(GameLoader.gameTime)

    if (Input.Keys.RIGHT == keycode)
      player.moveRight(GameLoader.gameTime)

    if (Input.Keys.UP == keycode)
      player.moveUp(GameLoader.gameTime)

    if (Input.Keys.DOWN == keycode)
      player.moveDown(GameLoader.gameTime)

    if (Input.Keys.Z == keycode)
      player.jump(GameLoader.gameTime)

    if (Input.Keys.X == keycode)
      player.attack(GameLoader.gameTime)

    true
  }

  override def keyUp(keycode: Int): Boolean = {
    if (!GameLoader.monsterDb.contains("player")) return false

    def player = GameLoader.monsterDb("player")

    if (Input.Keys.LEFT == keycode && !Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Input.Keys.RIGHT == keycode && !Gdx.input.isKeyPressed(Input.Keys.LEFT))
      player.stop(GameLoader.gameTime)

    if (Input.Keys.UP == keycode)
      player.fall(GameLoader.gameTime)

    if (Input.Keys.SPACE == keycode)
      player.attack(GameLoader.gameTime)

    true
  }

  override def mouseMoved(screenX: Int, screenY: Int): Boolean = {
    true
  }

  override def keyTyped(character: Char): Boolean = {
    true
  }


  override def touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean = {
    true
  }

  override def scrolled(amount: Int): Boolean = {
    true
  }

  override def touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean = {
    true
  }

  override def touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean = {
    true
  }
}

