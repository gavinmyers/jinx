import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.physics.box2d.World

/**
  * Created by gavin on 4/24/16.
  */
class Flames(name:String, room:Room, posX:Float, posY:Float) extends Scenery(name:String, room:Room, posX:Float, posY:Float) {
  this.sceneryAnimation = new Animation(Math.max(Math.random().toFloat / 4f + 0.15f, 0.20f), animationSheet(32),animationSheet(33),animationSheet(34),animationSheet(35))
}
