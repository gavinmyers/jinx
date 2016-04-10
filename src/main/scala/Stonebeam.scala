import com.badlogic.gdx.graphics.g2d.{Animation, TextureRegion}
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.World

import scala.collection.mutable.ListBuffer

class Stonebeam(name:String, world:World, as:ListBuffer[TextureRegion], posX:Float, posY:Float,override val scaleX:Float, override val scaleY:Float) extends Bullet(name:String, world:World, as:ListBuffer[TextureRegion], posX:Float, posY:Float, scaleX:Float, scaleY:Float) {
  attackAnimationRight = new Animation(0.05f, animationSheet(24),animationSheet(25),animationSheet(26),animationSheet(27))
  attackAnimationLeft  = new Animation(0.05f, animationSheet(24),animationSheet(25),animationSheet(26),animationSheet(27))
  life = 2.5f


  override def move(gameTime:Float): Unit = {
    body.setGravityScale(0.0f)
    val vel:Vector2 = body.getLinearVelocity
    if(mov_h.equalsIgnoreCase("R")) {
      vel.x += 0.2f
      sprite.setRegion(attackAnimationRight.getKeyFrame(gameTime, true))
    } else {
      vel.x -= 0.2f
      sprite.setRegion(attackAnimationLeft.getKeyFrame(gameTime, true))
    }
    vel.y = 0f
    body.setLinearVelocity(vel)
    if(life + created < GameLoader.gameTime) {
      destroy()

    }
  }
}
