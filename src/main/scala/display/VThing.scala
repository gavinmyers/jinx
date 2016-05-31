package display

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.{TextureRegion, Batch, Sprite}
import com.badlogic.gdx.physics.box2d._

import scala.collection.mutable.ListBuffer

trait VThing {
  def sprite:Sprite
  def scaleX:Float
  def scaleY:Float
  def posX:Float
  def posY:Float
  def body:Body
  def fixture: Fixture
}

object VThing {
  def sheet = new Texture("levels.png")
  val sheetTextures:ListBuffer[TextureRegion] = ListBuffer()
  for(tr <- TextureRegion.split(sheet, 24, 24)) {
    for(tx <- tr) {
      sheetTextures.append(tx)
    }
  }
}