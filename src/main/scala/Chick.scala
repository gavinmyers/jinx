import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.physics.box2d.World

import scala.collection.mutable.ListBuffer

object Chick {

    def sheet = new Texture("chick.png")
    val sheetTextures:ListBuffer[TextureRegion] = ListBuffer()
    for(tr <- TextureRegion.split(sheet, 24, 24)) {
        for(tx <- tr) {
            sheetTextures.append(tx)
        }
    }
}

class Chick(name:String,
    world:World,
    posX:Float,
    posY:Float,
    scaleX:Float,
    scaleY:Float)

  extends Being(name:String,
    world:World,
    Chick.sheetTextures,
    posX:Float,
    posY:Float,
    scaleX * 0.5f,
    scaleY * 0.5f) {

    this.life = 1

}
