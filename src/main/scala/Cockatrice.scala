import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.physics.box2d.World

import scala.collection.mutable.ListBuffer

object Cockatrice {
  def sheet = new Texture("cockatrice.png")
  val sheetTextures:ListBuffer[TextureRegion] = ListBuffer()
  for(tr <- TextureRegion.split(sheet, 24, 24)) {
    for(tx <- tr) {
      sheetTextures.append(tx)
    }
  }
}

class Cockatrice(name:String,
             world:World,
             posX:Float,
             posY:Float,
             scaleX:Float,
             scaleY:Float)

  extends Being(name:String,
    world:World,
    Cockatrice.sheetTextures,
    posX:Float,
    posY:Float,
    scaleX:Float,
    scaleY:Float) {

  this.canFly = true
  this.weapon = new Stonegaze(this)
}
