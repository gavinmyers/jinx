import box2dLight.{PointLight, RayHandler}
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType
import com.badlogic.gdx.graphics.{Color, GL20}
import com.badlogic.gdx.{ApplicationAdapter, Gdx, Input, InputProcessor}
import display.VRoom
import utils.Tiled

import scala.collection.JavaConversions._

class Sinx extends ApplicationAdapter with InputProcessor {
  println("Sinx")

  RayHandler.setGammaCorrection(true)
  RayHandler.useDiffuseLight(true)

  var debug: Boolean = false
  var downButtons: Float = 0

  var scene:VRoom = _

  override def create(): Unit = {
    val level01:generics.Room = Tiled.mapToRoom("level01")
    val forest01:generics.Room = Tiled.mapToRoom("forest01")
    scene = new VRoom("level01",level01)
    return

    Gdx.input.setInputProcessor(this)
    def mp3Sound:Sound = Gdx.audio.newSound(Gdx.files.internal("_ghost_-_Lullaby.mp3"))
    mp3Sound.play(0.5f)
    loadLevel("level01", "level01_w")
  }

  def loadLevel(level: String, target: String): Unit = {
    if (GameLoader.roomDb.contains(level)) {
      GameLoader.room = GameLoader.roomDb(level)
      GameLoader.room.start(target)

    } else {
      val room: Room = new Room(level)
      room.init(target)
      if (GameLoader.player == null) {
        GameLoader.player = new Lilac("player", room, 0, 0, 1.0f, 1.0f)
        //GameLoader.player.weapon = new
        GameLoader.player.brain = null
        GameLoader.player.runMaxVelocity = 5f
      }
      GameLoader.room = room
    }
    GameLoader.player.goto(GameLoader.room,GameLoader.room.startX, GameLoader.room.startY)
    /*
    GameLoader.player.light = new PointLight(GameLoader.room.handler, 128, new Color(1f, 1f, 1f, 0.4f), 24, 0, 0)
    GameLoader.player.light.attachToBody(GameLoader.player.body, 0, 0)
    GameLoader.player.light.setIgnoreAttachedBody(true)
    GameLoader.player.light.setContactFilter(0, 2, -1)
    */
  }


  override def render(): Unit = {
    scene.render()



    return

    val f:Float = 0.2f
    GameLoader.room.handler.setAmbientLight(f, f, f, 0.6f)
    if ("".equalsIgnoreCase(GameLoader.goto) == false) {
      loadLevel(GameLoader.goto, GameLoader.target)
      GameLoader.goto = ""
    }

    for (thing <- GameLoader.room.thingDb) {
      if (!thing.destroyed) {
        thing.update(GameLoader.gameTime)
      }
    }

    Gdx.gl.glClearColor(0, 0, 0, 1)
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

    GameLoader.gameTime += Gdx.graphics.getDeltaTime
    GameLoader.room.draw(debug)

    for (c <- GameLoader.room.world.getContactList) {

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

    if (Gdx.input.isKeyJustPressed(Input.Keys.D))
      debug = !debug

    def width:Float = Gdx.graphics.getWidth
    def height:Float = Gdx.graphics.getHeight() * 0.45f

    {
      var sr: ShapeRenderer = new ShapeRenderer()
      sr.setColor(Color.BLACK)
      sr.begin(ShapeType.Filled)
      sr.box(0f, 0f, 0f, width, height, 0f)
      sr.end()
    }

    {
      var sr: ShapeRenderer = new ShapeRenderer()
      sr.setColor(Color.DARK_GRAY)
      sr.begin(ShapeType.Filled)
      sr.box(0f, height, 0f, width, 6f, 0f)
      sr.end()
    }

    {
      def player = GameLoader.player
      var sr: ShapeRenderer = new ShapeRenderer()
      sr.setColor(Color.GRAY)
      sr.begin(ShapeType.Filled)
      sr.box(0f, height, 0f,width * (player.life / player.lifeMax), 4f, 0f)
      sr.end()
    }

  }

  override def dispose(): Unit = {
    println("dispose")
  }

  override def keyDown(keycode: Int): Boolean = {
    if (GameLoader.player == null) return false

    def player = GameLoader.player

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
    if (GameLoader.player == null) return false

    def player = GameLoader.player

    if (Input.Keys.LEFT == keycode && !Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Input.Keys.RIGHT == keycode && !Gdx.input.isKeyPressed(Input.Keys.LEFT))
      player.stop(GameLoader.gameTime)

    if (Input.Keys.UP == keycode)
      player.fall(GameLoader.gameTime)

    if (Input.Keys.DOWN == keycode)
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

