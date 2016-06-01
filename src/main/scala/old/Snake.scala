package old

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion

import scala.collection.mutable.ListBuffer

object Snake {
  def sheet = new Texture("snake.png")
  val sheetTextures:ListBuffer[TextureRegion] = ListBuffer()
  for(tr <- TextureRegion.split(sheet, 24, 24)) {
    for(tx <- tr) {
      sheetTextures.append(tx)
    }
  }
}

class Snake(name:String,
            room:Room,
            posX:Float,
            posY:Float,
            scaleX:Float,
            scaleY:Float)

  extends Being(name:String,
    room:Room,
    Snake.sheetTextures,
    posX:Float,
    posY:Float,
    scaleX:Float,
    scaleY:Float) {


}
