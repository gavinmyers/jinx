import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.physics.box2d.World

import scala.collection.mutable.ListBuffer

object Icebird {
  def sheet = new Texture("icebird.png")
  val sheetTextures:ListBuffer[TextureRegion] = ListBuffer()
  for(tr <- TextureRegion.split(sheet, 24, 24)) {
    for(tx <- tr) {
      sheetTextures.append(tx)
    }
  }
}

class Icebird(name:String,
              world:World,
              posX:Float,
              posY:Float,
              scaleX:Float,
              scaleY:Float)

  extends Being(name:String,
    world:World,
    Icebird.sheetTextures,
    posX:Float,
    posY:Float,
    scaleX:Float,
    scaleY:Float) {

  this.iceResistance = 1f
  this.fireResistance = -1f
  this.weapon = new Icebreath(this)
  this.canFly = true



}
