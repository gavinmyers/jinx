package old

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion

import scala.collection.mutable.ListBuffer

object Zombie {
  def sheet = new Texture("zombie.png")
  val sheetTextures:ListBuffer[TextureRegion] = ListBuffer()
  for(tr <- TextureRegion.split(sheet, 24, 24)) {
    for(tx <- tr) {
      sheetTextures.append(tx)
    }
  }
}

class Zombie(name:String,
              room:Room,
              posX:Float,
              posY:Float,
              scaleX:Float,
              scaleY:Float)

  extends Being(name:String,
    room:Room,
    Zombie.sheetTextures,
    posX:Float,
    posY:Float,
    scaleX:Float,
    scaleY:Float) {


}
