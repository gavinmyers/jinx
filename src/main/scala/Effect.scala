import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.{TextureRegion, Batch}
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.{Fixture, World}

import scala.collection.mutable.ListBuffer

object Effect {
  def sheet = new Texture("effects.png")
  val sheetTextures:ListBuffer[TextureRegion] = ListBuffer()
  for(tr <- TextureRegion.split(sheet, 12, 12)) {
    for(tx <- tr) {
      sheetTextures.append(tx)
    }
  }
}

class Effect(name:String, world:World, attacker:Thing, receiver:Thing, posX:Float, posY:Float,val scaleX:Float, val scaleY:Float) extends Thing() {

  this.created = GameLoader.gameTime
  var life = 1.3
  var fixture: Fixture = _

  override def init(): Unit = {
    GameLoader.effectDb += this
    GameLoader.thingDb += this
  }

  override def destroy() : Unit = {
    GameLoader.effectDb -= this
    super.destroy()
  }

  override def update(gameTime:Float): Unit = {

  }

  override def move(gameTime:Float): Unit = {
    this.body.setGravityScale(0f)
    val vel:Vector2 = body.getLinearVelocity
    vel.x = receiver.body.getLinearVelocity.x
    vel.y = receiver.body.getLinearVelocity.y
    body.setLinearVelocity(vel)

    if(life + created < GameLoader.gameTime) {
      destroy()
    }
  }

  override def draw(batch:Batch): Unit = {
    move(GameLoader.gameTime)
    super.draw(batch)
  }

}
