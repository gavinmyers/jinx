import box2dLight.{PointLight, RayHandler}
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.{OrthographicCamera, Texture, Color, GL20}
import com.badlogic.gdx.maps.MapObject
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
      {
        def r = GameLoader.levelMap
          .getLayers
          .get("positions")
          .getObjects
          .get("player_start")
          .asInstanceOf[RectangleMapObject]
          .getRectangle

        val p = new Lilac("player", GameLoader.world, r.x, r.y, 1.0f, 1.0f)
        p.light = new PointLight(GameLoader.handler, 128, new Color(1f, 1f, 1f, 0.8f), 24, 0, 0)
        p.light.attachToBody(p.body, 0, 0)
        p.light.setIgnoreAttachedBody(true)
        p.light.setContactFilter(0, 2, -1)
        p.brain = null
        p.runMaxVelocity = 5f

      }

      for(mo:MapObject <- GameLoader.levelMap.getLayers.get("positions").getObjects) {
        if("scenery_ember".equalsIgnoreCase(mo.getName)) {
          def r = mo.asInstanceOf[RectangleMapObject].getRectangle
          for (i <- 0.to(r.width.toInt) by 24) {
            new Embers("ember", GameLoader.world, r.x + i + 12, r.y + 12)
          }
        }
        if("scenery_fire".equalsIgnoreCase(mo.getName)) {
          def r = mo.asInstanceOf[RectangleMapObject].getRectangle
          for (i <- 0.to(r.width.toInt) by 24) {
            new Flames("fire", GameLoader.world, r.x + i + 12, r.y + 12)
          }
        }
      }

/*
      for (x <- 0 to 1) {
        var mod: Float = (((Math.random() * 125) + 20) / 100).toFloat
        var m = new Chick("monster", GameLoader.world, r.x + 248, r.y, 1.0f * mod, 1.0f * mod)
      }

*/
      /*
      for (x <- 0 to 5) {
        var mod: Float = (((Math.random() * 25) + 100) / 100).toFloat
        var m = new Icebird("monster", GameLoader.world, r.x + 248, r.y, 1.0f * mod, 1.0f * mod)
      }
      for (x <- 0 to 5) {
        var mod: Float = (((Math.random() * 125) + 60) / 100).toFloat
        var m = new Phoenix("monster", GameLoader.world, r.x + 248, r.y, 1.0f * mod, 1.0f * mod)
      }

            for (x <- 0 to 5) {
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
      */
    }

  }

  var debug: Boolean = false
  var initialPosition:Vector3 = new Vector3()

  override def render(): Unit = {
    for (thing <- GameLoader.thingDb) {
      if (!thing.destroyed) {
        thing.update(GameLoader.gameTime)
      }
    }

    Gdx.gl.glClearColor(0, 0, 0, 1)
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

    def lerp: Float = 99f
    val position: Vector3 = GameLoader.camera.position
    val backgroundPallalaxTmp:Array[Vector3] = Array.fill[Vector3](10)(new Vector3())


    if (GameLoader.monsterDb.contains("player")) {
      def player = GameLoader.monsterDb("player")
      position.x += (player.sprite.getX - position.x) * lerp * Gdx.graphics.getDeltaTime
      //position.y += (player.sprite.getY - position.y) * lerp * Gdx.graphics.getDeltaTime
      position.y = initialPosition.y

      if(initialPosition.x == 0 && player.sprite.getX != 0) {
        initialPosition.x = player.sprite.getX
        initialPosition.y = player.sprite.getY
      }

      if(player.sprite.getX != 0) {
        backgroundPallalaxTmp(0).y = position.y
        backgroundPallalaxTmp(0).x = initialPosition.x
        var spd = 0.1f
        for(i <- 1 to 9) {
          backgroundPallalaxTmp(i).x =  initialPosition.x - ((initialPosition.x - player.sprite.getX) * spd)
          backgroundPallalaxTmp(i).y = position.y
          spd += 0.1f
        }

      }
    }


    GameLoader.camera.position.set(position)

    GameLoader.camera.zoom = 0.5f
    GameLoader.parallalaxCameras.foreach(f => f.zoom = 0.5f)

    GameLoader.camera.update()
    GameLoader.parallalaxCameras.foreach(f => f.update())



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

    for(i <- 0 to 9) {
      GameLoader.parallalaxCameras(i).position.set(backgroundPallalaxTmp(i))
      GameLoader.batch.setProjectionMatrix(GameLoader.parallalaxCameras(i).combined)
      GameLoader.batch.begin()
      //GameLoader.levelMapRenderer.renderTileLayer(GameLoader.levelMap.getLayers().get("ladder").asInstanceOf[TiledMapTileLayer])
      GameLoader.levelMapRenderer.setView(GameLoader.parallalaxCameras(i))
      GameLoader.levelMapRenderer.render(Array(i))

      GameLoader.batch.end()
    }



    GameLoader.handler.setCombinedMatrix(GameLoader.camera)
    GameLoader.batch.setProjectionMatrix(GameLoader.camera.combined)
    GameLoader.batch.begin()
    GameLoader.font.draw(GameLoader.batch, "Hello World", 500, 500)

    for (thing <- GameLoader.sceneryDb) {
      if (!thing.destroyed) {
        thing.move(GameLoader.gameTime)
        thing.draw(GameLoader.batch)
      }
    }

    for (thing <- GameLoader.thingDb) {
      if (!thing.destroyed) {
        thing.move(GameLoader.gameTime)
        thing.draw(GameLoader.batch)
      }
    }
    GameLoader.batch.end()

    GameLoader.batch.begin()
    //GameLoader.levelMapRenderer.renderTileLayer(GameLoader.levelMap.getLayers().get("ladder").asInstanceOf[TiledMapTileLayer])
    GameLoader.levelMapRenderer.setView(GameLoader.camera)
    GameLoader.levelMapRenderer.render(Array(13, 14, 15, 16, 17, 18, 19, 20, 21, 22))
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

