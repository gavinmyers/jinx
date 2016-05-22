import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.physics.box2d.World

/**
  * Created by gavin on 4/24/16.
  */
class Embers(name:String, room:Room, posX:Float, posY:Float) extends Scenery(name:String, room:Room, posX:Float, posY:Float) {
  this.sceneryAnimation = new Animation(Math.max(Math.random().toFloat / 4f + 0.05f, 0.10f), animationSheet(36),animationSheet(37),animationSheet(38),animationSheet(39))
}
