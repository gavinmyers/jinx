
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.{TextureRegion, Animation}

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d._
import com.badlogic.gdx.{Input, ApplicationAdapter, Gdx}
import net.dermetfan.gdx.graphics.g2d.Box2DSprite

class Sinx extends ApplicationAdapter {
  println("Sinx")


  var walkRightAnimation:Animation = _
  var walkLeftAnimation:Animation = _

  var gameTime:Float = 0

  override def create(): Unit = {
    println("create")

    GameLoader.camera.setToOrtho(false)
    GameLoader.backgroundCamera.setToOrtho(false)

    GameLoader.createLevel()
    GameLoader.levelMapRenderer.setView(GameLoader.camera)
    GameLoader.monsterDb += "player" -> new Thing(GameLoader.world, GameLoader.player(0), BodyDef.BodyType.DynamicBody, 100, 150)
    GameLoader.monsterDb += "monster" -> new Thing(GameLoader.world, GameLoader.monsters(1), BodyDef.BodyType.DynamicBody, 100, 250)
    GameLoader.camera.translate(0, 0, 0)

    walkRightAnimation = new Animation(0.15f, GameLoader.player(4),GameLoader.player(5))
    walkLeftAnimation = new Animation(0.15f, GameLoader.player(6),GameLoader.player(7))

  }

  override def render(): Unit = {
    Gdx.gl.glClearColor(0, 0, 0, 1)
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
    gameTime = gameTime + Gdx.graphics.getDeltaTime()
    GameLoader.world.step(Gdx.graphics.getDeltaTime(), 6, 2)

    if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {

      //GameLoader.camera.position.add(-1, 0, 0)
      //GameLoader.backgroundCamera.position.add(-0.5f, 0, 0)

      GameLoader.monsterDb("player").moveLeft()
      GameLoader.monsterDb("player").sprite.setRegion(walkLeftAnimation.getKeyFrame(gameTime, true))
    }

    if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {

      //GameLoader.camera.position.add(1, 0, 0)
      //GameLoader.backgroundCamera.position.add(0.5f, 0, 0)

      GameLoader.monsterDb("player").moveRight()
      GameLoader.monsterDb("player").sprite.setRegion(walkRightAnimation.getKeyFrame(gameTime, true))

    }

    if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
      //GameLoader.backgroundCamera.position.add(0, 0.5f, 0)
      GameLoader.monsterDb("player").moveUp()
    }


    if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
      //GameLoader.backgroundCamera.position.add(0, -0.5f, 0)
      GameLoader.monsterDb("player").moveDown()
    }


    GameLoader.monsterDb("monster").moveRight()

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
    GameLoader.monsterDb("player").draw(GameLoader.batch)
    GameLoader.batch.end()



    GameLoader.debugRenderer.render(GameLoader.world, GameLoader.camera.combined)

  }

  override def dispose(): Unit = {
    println("dispose")
  }
}

