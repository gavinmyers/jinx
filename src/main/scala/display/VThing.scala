package display

import box2dLight.PositionalLight
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.{TextureRegion, Batch, Sprite}
import com.badlogic.gdx.physics.box2d._
import game.{Bullet, Tool, Creature, Thing}

import scala.collection.mutable.ListBuffer

trait VThing {
  def sprite:Sprite
  def scaleX:Float
  def scaleY:Float
  def startX:Float
  def startY:Float
  var lastX:Float = 0
  var lastY:Float = 0
  var light:PositionalLight = _
  def body:Body
  def fixture: Fixture
  def update(gameTime:Float):Unit = {
  }
}

object VThing {
  def sheet = new Texture("levels.png")
  val sheetTextures:ListBuffer[TextureRegion] = ListBuffer()
  for(tr <- TextureRegion.split(sheet, 24, 24)) {
    for(tx <- tr) {
      sheetTextures.append(tx)
    }
  }

  def create(thing:Thing, world:World):VThing = {
    if(thing.category == Thing.floor) {
      return new VTile(thing, world)

    } else if(thing.category == Thing.lilac) {
      return new VCreature(thing.asInstanceOf[Creature], world, VLilac.sheetTextures)

    } else if(thing.category == Thing.snake) {
      return new VCreature(thing.asInstanceOf[Creature], world, VSnake.sheetTextures)

    } else if(thing.category == Thing.lantern) {
      return new VTool(thing.asInstanceOf[Tool], world, VTool.sheetTextures(13))

    } else if(thing.category == Thing.ironsword) {
      return new VTool(thing.asInstanceOf[Tool], world, VTool.sheetTextures(25))

    } else if(thing.category == Thing.cupcake) {
      return new VTool(thing.asInstanceOf[Tool], world, VTool.sheetTextures(24))

    } else if(thing.category == Thing.pigmask) {
      return new VTool(thing.asInstanceOf[Tool], world, VTool.sheetTextures(18))

    } else if(thing.category == Thing.medicinewheel) {
      return new VTool(thing.asInstanceOf[Tool], world, VTool.sheetTextures(16))

    } else if(thing.category == Thing.bullet) {
      return new VBullet(thing.asInstanceOf[Bullet], world, VBullet.sheetTextures)
    }

    return new VInteraction(thing, world, VThing.sheetTextures(13))
  }


}