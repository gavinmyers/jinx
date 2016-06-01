package old

import com.badlogic.gdx.graphics.g2d.{Animation, TextureRegion}

import scala.collection.mutable.ListBuffer

class Burn(receiver: Thing, attacker: Thing, source: Thing, intensity: Float = 1.0f, life: Float = 2.0f, cooldown: Float = 1)
  extends Effect(receiver: Thing, attacker: Thing, source: Thing, intensity: Float, life: Float, cooldown: Float) {
  def animationSheet: ListBuffer[TextureRegion] = Effect.sheetTextures

  def effectAnimation = new Animation(0.15f, animationSheet(0), animationSheet(1), animationSheet(2), animationSheet(3))

  this.opposite = classOf[Freeze]

  override def move(gameTime: Float): Unit = {
    if (lastCooldown + cooldown < gameTime) {
      receiver.damage(this, 5 * this.intensity)
      lastCooldown = gameTime
    }
    sprite.setRegion(effectAnimation.getKeyFrame(gameTime, true))
    super.move(gameTime)
  }
}
