package old

import com.badlogic.gdx.graphics.g2d.{Animation, TextureRegion}
import com.badlogic.gdx.math.Vector2

import scala.collection.mutable.ListBuffer

class Fireball(name:String, room:Room, animationSheet:ListBuffer[TextureRegion], posX:Float, posY:Float, scaleX:Float,  scaleY:Float) extends Bullet(name:String, room:Room, animationSheet:ListBuffer[TextureRegion], posX:Float, posY:Float, scaleX:Float, scaleY:Float) {
  attackAnimationRight = new Animation(0.05f, animationSheet(16),animationSheet(17),animationSheet(18),animationSheet(19),animationSheet(20),animationSheet(21),animationSheet(22),animationSheet(23))
  attackAnimationLeft = new Animation(0.05f, animationSheet(16),animationSheet(17),animationSheet(18),animationSheet(19),animationSheet(20),animationSheet(21),animationSheet(22),animationSheet(23))
  life = 2.5f

  fire = true

  override def move(gameTime:Float): Unit = {
    body.setGravityScale(0.25f)
    val vel:Vector2 = body.getLinearVelocity
    if(mov_h.equalsIgnoreCase("R")) {
      vel.x += 0.2f
      sprite.setRegion(attackAnimationRight.getKeyFrame(gameTime, true))
    } else {
      vel.x -= 0.2f
      sprite.setRegion(attackAnimationLeft.getKeyFrame(gameTime, true))
    }
    vel.y += 0.1f
    if(!moving) {
      vel.x = body.getLinearVelocity.x * 0.8f
      vel.y = body.getLinearVelocity.y * 0.8f
      this.body.setGravityScale(0f)
    }
    body.setLinearVelocity(vel)
    if(life + created < GameLoader.gameTime) {
      destroy()

    }
  }
}
