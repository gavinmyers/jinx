import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.physics.box2d.World

import scala.collection.mutable.ListBuffer

object Lilac {
  def sheet = new Texture("jayden.png")
  val sheetTextures:ListBuffer[TextureRegion] = ListBuffer()
  for(tr <- TextureRegion.split(sheet, 24, 24)) {
    for(tx <- tr) {
      sheetTextures.append(tx)
    }
  }
}

class Lilac(name:String,
              room:Room,
              posX:Float,
              posY:Float,
              scaleX:Float,
              scaleY:Float)

  extends Being(name:String,
    room:Room,
    Lilac.sheetTextures,
    posX:Float,
    posY:Float,
    scaleX:Float,
    scaleY:Float) {

  override def init():Unit = {
    this.luminance = 0.5f
  }

  this.life = 100

  this.weapon = new Claw(this)
}
