import ai.{GenericAI, PlayerAI, AI}
import box2dLight.{PointLight, RayHandler}
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType
import com.badlogic.gdx.graphics.{Color, GL20}
import com.badlogic.gdx.{ApplicationAdapter, Gdx, Input, InputProcessor}
import display.{VInventory, VRoom}
import game.{Tool, GenericCreature, Entrance}
import old.GameLoader
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

  override def create(): Unit = {
    Tiled.load("level01")
    val level01:game.Room = Tiled.rooms("level01")
    for((k,thing) <- level01.inventory) {
      if(thing.category == game.Thing.entrance && thing.asInstanceOf[Entrance].default) {
        lilac = new GenericCreature
        lilac.id = "lilac"
        lilac.category = game.Thing.lilac
        lilac.location=level01
        lilac.startX=thing.startX
        lilac.startY=thing.startY + 50
        level01.enter(lilac)
        var playerBrain:AI = new PlayerAI
        lilac.ai = playerBrain



        val enemy:GenericCreature = new GenericCreature
        enemy.category = game.Thing.lilac
        enemy.location=level01
        enemy.startX=thing.startX + 200
        enemy.startY=thing.startY + 50
        var enemyBrain:AI = new GenericAI
        enemy.ai = enemyBrain
        val enemysword:IronSword = new IronSword
        enemy.enter(enemysword)
        enemy.holding = enemysword
        //level01.enter(enemy)

        /*
        val lantern:Lantern = new Lantern
        lantern.startX=thing.startX + 50
        lantern.startY=thing.startY + 25
        level01.enter(lantern)

        val p:Pigmask = new Pigmask
        p.startX=thing.startX + 50
        p.startY=thing.startY + 50
        level01.enter(p)

        val c:Cupcake = new Cupcake
        c.startX=thing.startX + 75
        c.startY=thing.startY + 75
        level01.enter(c)

        val m:MedicineWheel = new MedicineWheel
        m.startX=thing.startX + 125
        m.startY=thing.startY + 125
        level01.enter(m)

        val ironsword:IronSword = new IronSword
        ironsword.startX=thing.startX + 70
        ironsword.startY=thing.startY + 25
        level01.enter(ironsword)
        */
      }
    }
    inventory = new VInventory(lilac)
    scene = new VRoom(lilac.location.id, lilac.location.asInstanceOf[game.Room])

    Gdx.input.setInputProcessor(this)

    def mp3Sound:Sound = Gdx.audio.newSound(Gdx.files.internal("_ghost_-_Lullaby.mp3"))
    mp3Sound.play(0.5f)
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

    if (Input.Keys.Z == keycode)
      lilac.jump = true

    if (Input.Keys.D == keycode)
      lilac.pickup = true

    if (Input.Keys.S == keycode)
      lilac.use(gameTime)

    if (Input.Keys.X == keycode)
      lilac.attack(gameTime)

    true
  }

  override def keyUp(keycode: Int): Boolean = {

    if (Input.Keys.LEFT == keycode && !Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Input.Keys.RIGHT == keycode && !Gdx.input.isKeyPressed(Input.Keys.LEFT))
      lilac.stop()

    if (Input.Keys.D == keycode)
      lilac.pickup = false

    if (Input.Keys.I == keycode)
      showInventory = showInventory == false
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

