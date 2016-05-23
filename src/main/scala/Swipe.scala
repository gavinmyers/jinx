import com.badlogic.gdx.graphics.g2d.{Animation, TextureRegion}

import scala.collection.mutable.ListBuffer

class Swipe(name:String, room:Room, animationSheet:ListBuffer[TextureRegion], posX:Float, posY:Float, scaleX:Float,  scaleY:Float) extends Bullet(name:String, room:Room, animationSheet:ListBuffer[TextureRegion], posX:Float, posY:Float, scaleX:Float, scaleY:Float) {
  this.attackAnimationRight = new Animation(0.15f, animationSheet(0),animationSheet(1),animationSheet(2))
  this.attackAnimationLeft = new Animation(0.15f, animationSheet(8),animationSheet(9),animationSheet(10))
  lightIntensity = 0f
}
