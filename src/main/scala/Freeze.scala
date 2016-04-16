import com.badlogic.gdx.graphics.g2d.{Batch, TextureRegion, Animation, Sprite}
import com.badlogic.gdx.physics.box2d._

import scala.collection.mutable.ListBuffer

class Freeze(receiver:Thing, attacker:Thing, source:Thing, intensity:Float = 1.0f, life:Float = 2.0f, cooldown:Float = 1)
  extends Effect(receiver:Thing, attacker:Thing, source:Thing, intensity:Float, life:Float, cooldown:Float) {
  def animationSheet:ListBuffer[TextureRegion] = Effect.sheetTextures
  def effectAnimation = new Animation(0.15f, animationSheet(8),animationSheet(9),animationSheet(10),animationSheet(11))

  override def move(gameTime:Float): Unit = {
    if(lastCooldown + cooldown < gameTime) {
      receiver.damage(this, 5)
      lastCooldown = gameTime
    }
    sprite.setRegion(effectAnimation.getKeyFrame(gameTime, true))
    super.move(gameTime)
  }
}
