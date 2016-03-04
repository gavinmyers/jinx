
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d._
import com.badlogic.gdx.{Input, ApplicationAdapter, Gdx}
import net.dermetfan.gdx.graphics.g2d.Box2DSprite

class Sinx extends ApplicationAdapter {
  println("Sinx")

  override def create(): Unit = {
    println("create")

    GameLoader.camera.setToOrtho(false)

    GameLoader.createLevel()
    GameLoader.levelMapRenderer.setView(GameLoader.camera)
    GameLoader.monsterDb += "player" -> new Thing(GameLoader.world, GameLoader.monsters(4), BodyDef.BodyType.DynamicBody, 100, 150)
    GameLoader.monsterDb += "monster" -> new Thing(GameLoader.world, GameLoader.monsters(1), BodyDef.BodyType.DynamicBody, 100, 250)
    GameLoader.camera.translate(0, 0, 0)

  }

  override def render(): Unit = {
    Gdx.gl.glClearColor(0, 0, 0, 1)
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
    GameLoader.world.step(Gdx.graphics.getDeltaTime(), 6, 2)

    if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
      GameLoader.monsterDb("player").moveLeft()
      GameLoader.camera.position.add(-1, 0, 0)
    }

    if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
      GameLoader.monsterDb("player").moveRight()
      GameLoader.camera.position.add(1, 0, 0)
    }

    if(Gdx.input.isKeyPressed(Input.Keys.UP))
      GameLoader.monsterDb("player").moveUp()

    if(Gdx.input.isKeyPressed(Input.Keys.DOWN))
      GameLoader.monsterDb("player").moveDown()

    GameLoader.monsterDb("monster").moveRight()

    GameLoader.camera.update()
    GameLoader.batch.setProjectionMatrix(GameLoader.camera.combined)

    GameLoader.batch.begin()
    //GameLoader.levelMapRenderer.renderTileLayer(GameLoader.levelMap.getLayers().get("sky").asInstanceOf[TiledMapTileLayer])
    GameLoader.levelMapRenderer.setView(GameLoader.camera)
    GameLoader.levelMapRenderer.render()
    GameLoader.batch.end()


    GameLoader.batch.begin()
    GameLoader.font.draw(GameLoader.batch, "Hello World", 500, 500)
    Box2DSprite.draw(GameLoader.batch, GameLoader.world)
    GameLoader.batch.end()



    GameLoader.debugRenderer.render(GameLoader.world, GameLoader.camera.combined)

  }

  override def dispose(): Unit = {
    println("dispose")
  }
}

