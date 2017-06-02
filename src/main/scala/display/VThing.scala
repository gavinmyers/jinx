package display

import box2dLight.PositionalLight
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.{TextureRegion, Batch, Sprite}
import com.badlogic.gdx.physics.box2d._
import game._

import scala.collection.mutable.ListBuffer

protected trait VThing {
  def sprite:Sprite
  def scaleX:Float
  def scaleY:Float
  def startX:Float
  def startY:Float
  var destroyed:Boolean = false
  var lastX:Float = 0
  var lastY:Float = 0
  var light:PositionalLight = _
  def body:Body
  def fixture: Fixture
  def update(gameTime:Float):Unit = {
  }
}

protected object VThing {
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

    } else if(thing.category == Thing.ladder) {
      return new VLadder(thing, world, VTool.tools("ladder"))

    } else if(thing.category == Thing.woodblock) {
      return new VTool(thing.asInstanceOf[Tool], world, VTool.tools("woodblock"))

    } else if(thing.category == Thing.woodcolumn) {
      return new VTool(thing.asInstanceOf[Tool], world, VTool.tools("woodcolumn"))


    } else if(thing.category == Thing.lilac) {
      return new VCreature(thing.asInstanceOf[Creature], world, VCreature.lilac.sheetTextures)

    } else if(thing.category == Thing.spider) {
      return new VCreature(thing.asInstanceOf[Creature], world, VCreature.spider.sheetTextures)

    } else if(thing.category == Thing.snake) {
      return new VCreature(thing.asInstanceOf[Creature], world, VCreature.snake.sheetTextures)

    } else if(thing.category == Thing.phoenix) {
      return new VCreature(thing.asInstanceOf[Creature], world, VCreature.phoenix.sheetTextures)

    } else if(thing.category == Thing.skeletonwarrior) {
      return new VCreature(thing.asInstanceOf[Creature], world, VCreature.skeletonwarrior.sheetTextures)

    } else if(thing.category == Thing.lantern) {
      return new VTool(thing.asInstanceOf[Tool], world, VTool.tools("lantern"))

    } else if(thing.category == Thing.ironsword) {
      return new VTool(thing.asInstanceOf[Tool], world, VTool.tools("ironsword"))

    } else if(thing.category == Thing.cupcake) {
      return new VTool(thing.asInstanceOf[Tool], world, VTool.tools("cupcake"))

    } else if(thing.category == Thing.catchem) {
      return new VTool(thing.asInstanceOf[Tool], world, VTool.tools("catchem"))


    } else if(thing.category == Thing.pigmask) {
      return new VTool(thing.asInstanceOf[Tool], world, VTool.tools("pigmask"))

    } else if(thing.category == Thing.medicinewheel) {
      return new VTool(thing.asInstanceOf[Tool], world, VTool.tools("medicinewheel"))

    } else if(thing.category == Thing.key) {
      return new VTool(thing.asInstanceOf[Tool], world, VTool.tools("key"))

    } else if(thing.category == Thing.chest) {
      return new VTool(thing.asInstanceOf[Tool], world, VTool.tools("chest"))

    } else if(thing.category == Thing.corpse) {
      return new VTool(thing.asInstanceOf[Tool], world, VTool.tools("corpse"))

    } else if(thing.category == Thing.bullet) {
      return new VBullet(thing.asInstanceOf[Bullet], world, VBullet.sheetTextures)

    } else if(thing.category == Thing.notification) {
      return new VNotification(thing.asInstanceOf[Notification] ,world)
    }

    return new VInteraction(thing, world, VThing.sheetTextures(13))
  }


}