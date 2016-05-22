import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.physics.box2d.World

import scala.collection.mutable.ListBuffer

object Phoenix {
  def sheet = new Texture("phoenix.png")
  val sheetTextures:ListBuffer[TextureRegion] = ListBuffer()
  for(tr <- TextureRegion.split(sheet, 24, 24)) {
    for(tx <- tr) {
      sheetTextures.append(tx)
    }
  }
}

class Phoenix(name:String,
            room:Room,
            posX:Float,
            posY:Float,
            scaleX:Float,
            scaleY:Float)

  extends Being(name:String,
    room:Room,
    Phoenix.sheetTextures,
    posX:Float,
    posY:Float,
    scaleX:Float,
    scaleY:Float) {

  this.fireResistance = 1f
  this.iceResistance = -1f

  this.weapon = new Firebreath(this)
  this.canFly = true



}
