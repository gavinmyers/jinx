package old

import com.badlogic.gdx.graphics.g2d.{Animation, TextureRegion}

import scala.collection.mutable.ListBuffer

class Petrification(receiver:Thing, attacker:Thing, source:Thing, intensity:Float = 1.0f, life:Float = 30.0f, cooldown:Float = 30)
  extends Effect(receiver:Thing, attacker:Thing, source:Thing, intensity:Float, life:Float, cooldown:Float) {
  def animationSheet:ListBuffer[TextureRegion] = Effect.sheetTextures
  def effectAnimation = new Animation(1f, animationSheet(16),animationSheet(17),animationSheet(18),animationSheet(19))

  override def move(gameTime:Float): Unit = {
    if(lastCooldown + cooldown < gameTime) {
      receiver.petrify()
      lastCooldown = gameTime
    }
    sprite.setRegion(effectAnimation.getKeyFrame(gameTime, true))
    super.move(gameTime)
  }
}

