
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.{TextureRegion, Animation}
import com.badlogic.gdx.maps.MapObject
import com.badlogic.gdx.maps.objects.RectangleMapObject

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.math.{Rectangle, Vector2}
import com.badlogic.gdx.physics.box2d._
import com.badlogic.gdx.{Input, ApplicationAdapter, Gdx}
import net.dermetfan.gdx.graphics.g2d.Box2DSprite
import scala.collection.JavaConversions._

class Sinx extends ApplicationAdapter {
  println("Sinx")

  var gameTime:Float = 0

  override def create(): Unit = {
    println("create")

    GameLoader.camera.setToOrtho(false)
    GameLoader.backgroundCamera.setToOrtho(false)

    GameLoader.createLevel()
    GameLoader.levelMapRenderer.setView(GameLoader.camera)
    for(mo:MapObject <- GameLoader.levelMap.getLayers.get("positions").getObjects) {
      if(mo.getName.equalsIgnoreCase("player_start")) {
        def r:Rectangle = mo.asInstanceOf[RectangleMapObject].getRectangle
        GameLoader.monsterDb += "player" -> new Being(GameLoader.world, GameLoader.player(0), BodyDef.BodyType.DynamicBody, r.x, r.y)
      }
    }

    //GameLoader.monsterDb += "monster" -> new Being(GameLoader.world, GameLoader.monsters(1), BodyDef.BodyType.DynamicBody, 100, 250)
    GameLoader.camera.translate(0, 0, 0)

  }

  override def render(): Unit = {
    Gdx.gl.glClearColor(0, 0, 0, 1)
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
    gameTime = gameTime + Gdx.graphics.getDeltaTime()
    GameLoader.world.step(Gdx.graphics.getDeltaTime(), 6, 2)

    if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
      //GameLoader.camera.position.add(-1, 0, 0)
      //GameLoader.backgroundCamera.position.add(-0.5f, 0, 0)
      GameLoader.monsterDb("player").moveLeft(gameTime)
    }

    if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
      //GameLoader.camera.position.add(1, 0, 0)
      //GameLoader.backgroundCamera.position.add(0.5f, 0, 0)
      GameLoader.monsterDb("player").moveRight(gameTime)
    }

    if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
      //GameLoader.backgroundCamera.position.add(0, 0.5f, 0)
      GameLoader.monsterDb("player").moveUp(gameTime)
    }

    if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) {

      //GameLoader.backgroundCamera.position.add(0, -0.5f, 0)
      GameLoader.monsterDb("player").moveDown(gameTime)
    }


    //GameLoader.monsterDb("monster").moveRight(gameTime)

    GameLoader.camera.update()
    GameLoader.backgroundCamera.update()

    GameLoader.batch.setProjectionMatrix(GameLoader.backgroundCamera.combined)
    GameLoader.batch.begin()
    //GameLoader.levelMapRenderer.renderTileLayer(GameLoader.levelMap.getLayers().get("sky").asInstanceOf[TiledMapTileLayer])
    GameLoader.levelMapRenderer.setView(GameLoader.backgroundCamera)
    GameLoader.levelMapRenderer.render(Array(0,1,2))
    GameLoader.batch.end()


    GameLoader.batch.setProjectionMatrix(GameLoader.camera.combined)
    GameLoader.batch.begin()
    GameLoader.font.draw(GameLoader.batch, "Hello World", 500, 500)

    GameLoader.drawThings()
    GameLoader.batch.end()

    GameLoader.debugRenderer.render(GameLoader.world, GameLoader.camera.combined)

  }



  override def dispose(): Unit = {
    println("dispose")
  }
}

