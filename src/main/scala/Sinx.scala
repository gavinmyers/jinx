import javafx.scene.input.KeyCode

import ai.{AI, GenericAI, PlayerAI}
import box2dLight.{PointLight, RayHandler}
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType
import com.badlogic.gdx.graphics.{Color, GL20}
import com.badlogic.gdx.{ApplicationAdapter, Gdx, Input, InputProcessor}
import display.{VInventory, VRoom}
import game._
import tools._
import utils.Tiled

import scala.collection.JavaConversions._


class Sinx extends ApplicationAdapter with InputProcessor {
  println("Sinx")
  var gameTime:Float = 0
  RayHandler.setGammaCorrection(true)
  RayHandler.useDiffuseLight(true)

  var debug: Boolean = false
  var downButtons: Float = 0

  var scene:VRoom = _
  var inventory:VInventory = _
  var lilac:GenericCreature = _
  var showInventory:Boolean = false
  var currentRoom:game.Room = _

  override def create(): Unit = {
    Tiled.load("level01")
    currentRoom = Tiled.rooms("level01")
    currentRoom.alert = "Welcome to JINX, we're still testing basic game functionality so there isn't much to do right now. This is a long message, I hope word wrapping works the way it says it should otherwise this will be hard to read."
    for((k,thing) <- currentRoom.inventory) {
      if(thing.category == game.Thing.entrance && thing.asInstanceOf[Entrance].default) {
        lilac = new GenericCreature
        lilac.id = "lilac"
        lilac.category = game.Thing.lilac
        lilac.location=currentRoom
        lilac.startX=thing.startX
        lilac.startY=thing.startY + 50
        currentRoom.add(lilac)
        var playerBrain:AI = new PlayerAI
        lilac.ai = playerBrain

      }
    }
    inventory = new VInventory(lilac)
    scene = new VRoom(lilac.location.id, lilac.location.asInstanceOf[game.Room])

    Gdx.input.setInputProcessor(this)

    def mp3Sound:Sound = Gdx.audio.newSound(Gdx.files.internal("_ghost_-_Lullaby.mp3"))
    //mp3Sound.play(0.5f)
  }

  override def render(): Unit = {
    if(showInventory) {
      inventory.render()
      return
    }
    gameTime += Gdx.graphics.getDeltaTime

    if(scene.at(lilac.location.asInstanceOf[game.Room]) == false) {
      //TODO: Cleanup old VROOMs
      scene = new VRoom(lilac.location.id, lilac.location.asInstanceOf[game.Room])
      currentRoom = lilac.location.asInstanceOf[game.Room]
    }
      /*
        currentRoom.title = "JINX "
        currentRoom.title += "HP " + lilac.get("health").current + "[" + lilac.get("health").maximum + "]"
        currentRoom.title += "| HUNGER " + lilac.get("fullness").current + "[" + lilac.get("fullness").maximum + "]"
        currentRoom.title += "| WEIGHT " + lilac.get("encumbrance").current + " [" + lilac.get("encumbrance").maximum + "]"
        */
    var jv = lilac.get("jump_velocity")
    var jvmax = jv.maximum
    var jvc = jv.current
    var jvmin = jv.minimum
    var rv = lilac.get("run_velocity")
    var rvmax = rv.maximum
    var rvc = rv.current
    var rvmin = rv.minimum

    var isj = lilac.isJumping
    var isf = lilac.isFalling
    var cj = lilac.canJump

      currentRoom.title = s" JV [$jvmax ($jvc) $jvmin] "
      currentRoom.title += s" RV [$rvmax ($rvc) $rvmin] "
    currentRoom.title += s" J [$cj $isj $isf] "


    currentRoom.history = ""
    for((k,thing) <- lilac.near) {
      if(thing.description.length > 0) {
        currentRoom.history = "> "+thing.description+" \n"
      }
    }

    scene.render(lilac.lastX, lilac.lastY, gameTime)

    /*
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
      var sr: ShapeRenderer = new ShapeRenderer()
      sr.setColor(Color.GRAY)
      sr.begin(ShapeType.Filled)
      sr.box(0f, height, 0f,width * (lilac.healthCurrent / lilac.healthMax), 4f, 0f)
      sr.end()
    }
    */

  }

  override def dispose(): Unit = {
    println("dispose")
  }

  override def keyDown(keycode: Int): Boolean = {


    if (Input.Keys.LEFT == keycode)
      lilac.moveLeft()

    if (Input.Keys.RIGHT == keycode)
      lilac.moveRight()

    if (Input.Keys.UP == keycode)
      lilac.moveUp()

    if (Input.Keys.DOWN == keycode)
      lilac.moveDown()

    if (Input.Keys.D == keycode)
      lilac.drop()

    if (Input.Keys.Z == keycode)
      lilac.jump = true

    if (Input.Keys.P == keycode)
      lilac.pickup (gameTime)

    if (Input.Keys.C == keycode)
      lilac.use(gameTime)

    if (Input.Keys.X == keycode)
      lilac.attack(gameTime)

    if (Input.Keys.SPACE == keycode)
      currentRoom.alert = ""

    if (Input.Keys.ESCAPE == keycode)
      currentRoom.alert = "paused"

    if (Input.Keys.E == keycode)
        lilac.exit(gameTime)

    true
  }



  override def keyUp(keycode: Int): Boolean = {

    if (Input.Keys.LEFT == keycode && !Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Input.Keys.RIGHT == keycode && !Gdx.input.isKeyPressed(Input.Keys.LEFT))
      lilac.stop()

    if (Input.Keys.UP == keycode && !Gdx.input.isKeyPressed(Input.Keys.UP))
      lilac.stopJump()


    if (Input.Keys.O == keycode) {
      val t:Thing = lilac.open (gameTime)
      if(t != null) {
        inventory = new VInventory(t)
        showInventory = showInventory == false
      } else {
        showInventory = false
      }
    }
    if (Input.Keys.I == keycode) {
      inventory = new VInventory(lilac)
      showInventory = showInventory == false
    }
    /*
        if (Input.Keys.UP == keycode)
          player.fall(GameLoader.gameTime)

        if (Input.Keys.DOWN == keycode)
          player.fall(GameLoader.gameTime)

        if (Input.Keys.SPACE == keycode)
          player.attack(GameLoader.gameTime)
    */
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

