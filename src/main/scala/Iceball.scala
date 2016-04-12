import com.badlogic.gdx.graphics.g2d.{Animation, TextureRegion}
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.World

import scala.collection.mutable.ListBuffer

class Iceball(name:String, world:World, animationSheet:ListBuffer[TextureRegion], posX:Float, posY:Float,override val scaleX:Float, override val scaleY:Float) extends Bullet(name:String, world:World, animationSheet:ListBuffer[TextureRegion], posX:Float, posY:Float, scaleX:Float, scaleY:Float) {
  attackAnimationRight = new Animation(0.05f, animationSheet(32),animationSheet(33),animationSheet(34),animationSheet(35),animationSheet(36),animationSheet(37),animationSheet(38),animationSheet(37))
  attackAnimationLeft = new Animation(0.05f, animationSheet(32),animationSheet(33),animationSheet(34),animationSheet(35),animationSheet(36),animationSheet(37),animationSheet(38),animationSheet(37))
  life = 2.5f

  ice = true

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

  override def contact(thing:Thing) : Unit = {
    if(thing == null) {
    } else thing match {
      case _: Brick =>
        moving = false
      case _: Being if !contactList.contains(thing) && thing != attacker =>
        contactList += thing
        thing.damage(this, 1)
      case _ =>
    }
  }
}
