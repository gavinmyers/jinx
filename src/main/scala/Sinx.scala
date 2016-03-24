

import box2dLight.RayHandler
import com.badlogic.gdx.graphics.{Color, GL20}
import com.badlogic.gdx.graphics.g2d.{TextureRegion, Animation}
import com.badlogic.gdx.maps.MapObject
import com.badlogic.gdx.maps.objects.RectangleMapObject

import com.badlogic.gdx.maps.tiled.{TiledMapTile, TiledMapTileLayer}
import com.badlogic.gdx.math.{Matrix4, Rectangle, Vector2}
import com.badlogic.gdx.physics.box2d._
import com.badlogic.gdx.{Input, ApplicationAdapter, Gdx}
import net.dermetfan.gdx.graphics.g2d.Box2DSprite
import scala.collection.JavaConversions._



class Sinx extends ApplicationAdapter {
  println("Sinx")

  RayHandler.setGammaCorrection(true)
  RayHandler.useDiffuseLight(true)


  override def create(): Unit = {
    GameLoader.create()

    createLevel()

    def r = GameLoader.levelMap
      .getLayers
      .get("positions")
      .getObjects
      .get("player_start")
      .asInstanceOf[RectangleMapObject]
      .getRectangle

    new Being("player",GameLoader.world, GameLoader.player.get(0), BodyDef.BodyType.DynamicBody, r.x, r.y)

    //new Being("monster",GameLoader.world, GameLoader.player.get(0), BodyDef.BodyType.DynamicBody, r.x + 24, r.y + 24)

  }

  var debug:Boolean = false
  override def render(): Unit = {


    Gdx.gl.glClearColor(0, 0, 0, 1)
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
    GameLoader.gameTime += Gdx.graphics.getDeltaTime()
    GameLoader.world.step(Gdx.graphics.getDeltaTime(), 6, 2)

    def player = GameLoader.monsterDb("player")
    GameLoader.camera.position.set(player.sprite.getX, player.sprite.getY, 0)

    GameLoader.camera.zoom = 1f
    GameLoader.camera.update()
    GameLoader.batch.setProjectionMatrix(GameLoader.camera.combined)


    GameLoader.batch.begin()
    //GameLoader.levelMapRenderer.renderTileLayer(GameLoader.levelMap.getLayers().get("sky").asInstanceOf[TiledMapTileLayer])
    //GameLoader.levelMapRenderer.setView(GameLoader.backgroundCamera)
    //GameLoader.levelMapRenderer.render(Array(0,1,2))
    GameLoader.batch.end()


    GameLoader.batch.begin()
    GameLoader.font.draw(GameLoader.batch, "Hello World", 500, 500)
    drawThings()
    GameLoader.batch.end()




    def debugMatrix:Matrix4 = GameLoader.batch.getProjectionMatrix().cpy().scale(GameLoader.BOX_TO_WORLD, GameLoader.BOX_TO_WORLD, 0f)
    GameLoader.handler.setCombinedMatrix(debugMatrix)
    GameLoader.handler.updateAndRender()


    if(Gdx.input.isKeyJustPressed(Input.Keys.D))
      debug = debug == false

    if(debug)
      GameLoader.debugRenderer.render(GameLoader.world, debugMatrix)

    if(Gdx.input.isKeyPressed(Input.Keys.LEFT))
      player.moveLeft(GameLoader.gameTime)

    if(Gdx.input.isKeyPressed(Input.Keys.RIGHT))
      player.moveRight(GameLoader.gameTime)

    if(Gdx.input.isKeyPressed(Input.Keys.UP))
      player.moveUp(GameLoader.gameTime)

    if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE))
      player.attack(GameLoader.gameTime)

  }

  def abandon():Unit = {
    def player = GameLoader.monsterDb("player")
    GameLoader.camera.position.set(player.sprite.getX, player.sprite.getY, 0)
    GameLoader.camera.zoom = .01f
    //GameLoader.backgroundCamera.position.set(player.sprite.getX, player.sprite.getY, 0)
    GameLoader.camera.update()
    //GameLoader.backgroundCamera.update()
    //GameLoader.handler.setCombinedMatrix(GameLoader.camera)
    GameLoader.batch.setProjectionMatrix(GameLoader.camera.combined)

    GameLoader.gameTime += Gdx.graphics.getDeltaTime()
    GameLoader.world.step(Gdx.graphics.getDeltaTime(), 6, 2)

    if(Gdx.input.isKeyPressed(Input.Keys.LEFT))
      player.moveLeft(GameLoader.gameTime)

    if(Gdx.input.isKeyPressed(Input.Keys.RIGHT))
      player.moveRight(GameLoader.gameTime)

    if(Gdx.input.isKeyPressed(Input.Keys.UP))
      player.moveUp(GameLoader.gameTime)


    if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE))
      player.attack(GameLoader.gameTime)




    GameLoader.batch.begin()
    //GameLoader.levelMapRenderer.renderTileLayer(GameLoader.levelMap.getLayers().get("sky").asInstanceOf[TiledMapTileLayer])
    GameLoader.levelMapRenderer.setView(GameLoader.backgroundCamera)
    GameLoader.levelMapRenderer.render(Array(0,1,2))
    GameLoader.batch.end()



    GameLoader.batch.begin()
    GameLoader.font.draw(GameLoader.batch, "Hello World", 500, 500)
    drawThings()
    GameLoader.batch.end()

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



  }

  override def dispose(): Unit = {
    println("dispose")
  }


  def drawThings(): Unit = {
    for(bullet <- GameLoader.bulletDb) {

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
    for(thing <- GameLoader.thingDb) {
      thing.update(GameLoader.batch)
      thing.draw(GameLoader.batch)
    }
  }

  def createLevel(): Unit = {
    val tileX:Int = 24
    val tileY:Int = 24
    val tmtl = GameLoader.levelMap.getLayers.get("ground").asInstanceOf[TiledMapTileLayer]
    var x:Int = 0
    var y:Int = 0

    for( x <- 0 to tmtl.getWidth) {
      for(y <- 0 to tmtl.getHeight ){
        val c = tmtl.getCell(x,y)
        if(c != null) {
          val posX:Int = x * tileX + 12
          val posY:Int = y * tileY + 12
          val t:TiledMapTile = c.getTile()
          new Brick("ground_"+x+"_"+y, GameLoader.world, t.getTextureRegion(), BodyDef.BodyType.StaticBody, posX, posY)
        }
      }
    }
  }
}

