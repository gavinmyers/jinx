
import com.badlogic.gdx.graphics.GL20
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
    GameLoader.monsterDb += "player" -> new Thing(GameLoader.world, GameLoader.monsters(4), BodyDef.BodyType.DynamicBody, 100, 150)
    //GameLoader.camera.translate(0, 0, 0)

  }

  override def render(): Unit = {
    Gdx.gl.glClearColor(0, 0, 0, 1)
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
    GameLoader.world.step(Gdx.graphics.getDeltaTime(), 6, 2)


    var vel:Vector2 = GameLoader.monsterDb("player").body.getLinearVelocity()
    if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
      vel.x -= 5
      GameLoader.camera.position.add(-1, 0, 0)
    }
    if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
      vel.x += 5
      GameLoader.camera.position.add(1, 0, 0)

    }
    if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
      vel.y += 5
    }
    if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
      vel.y -= 5
    }

    GameLoader.monsterDb("player").body.setLinearVelocity(vel);
    GameLoader.camera.update()

    GameLoader.batch.begin()
    Box2DSprite.draw(GameLoader.batch, GameLoader.world)
    GameLoader.font.draw(GameLoader.batch, "Hello World", 200, 200)
    GameLoader.batch.end()
    GameLoader.batch.setProjectionMatrix(GameLoader.camera.combined)
    GameLoader.debugRenderer.render(GameLoader.world, GameLoader.camera.combined)

  }

  override def dispose(): Unit = {
    println("dispose")
  }
}

