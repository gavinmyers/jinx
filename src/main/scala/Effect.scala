import javax.sound.midi.Receiver

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.{Sprite, TextureRegion, Batch}
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d._

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

class Effect(var receiver:Thing, var attacker:Thing, var source:Thing, var intensity:Float = 1.0f, var life:Float = 1.3f, var cooldown:Float = 1) extends Thing(receiver.location) {

  this.created = GameLoader.gameTime
  var lastCooldown = this.created
  var opposite:AnyRef = _
  var fixture: Fixture = _


  override def init(): Unit = {
    this.sprite = new Sprite(Effect.sheetTextures.head)
    this.sprite.setScale(scaleX, scaleY)

    var width = sprite.getWidth * scaleX
    var height = sprite.getHeight * scaleY

    this.body = receiver.location.world
      .createBody(
        {val b: BodyDef = new BodyDef()
          val x = receiver.sprite.getX + (receiver.sprite.getWidth / 2)
          val y = receiver.sprite.getY + receiver.sprite.getHeight
          b.`type` = BodyDef.BodyType.DynamicBody
          b.fixedRotation = true
          b.position.set(GameUtil.pixelsToMeters(x), GameUtil.pixelsToMeters(y))
          b})

    this.fixture = body.createFixture(
      {val f:FixtureDef = new FixtureDef()
        var shape:PolygonShape = new PolygonShape()
        f.isSensor = true
        f.shape = shape
        shape.setAsBox(GameUtil.pixelsToMeters(width / 2f), GameUtil.pixelsToMeters(height / 2f))
        f.friction = 0f; f})

    body.setUserData(sprite)
    fixture.setUserData(this)
    receiver.location.effectDb += this
    receiver.location.thingDb += this
  }

  def grow(): Unit = {
    this.scaleX += 0.25f
    this.scaleY += 0.25f
    this.intensity += 0.25f
    this.life = this.life * this.intensity
    this.created = GameLoader.gameTime
  }

  def shrink(): Unit = {
    this.scaleX -= 0.25f
    this.scaleY -= 0.25f
    this.intensity -= 0.25f
    this.life = this.life - this.intensity
    this.created += (this.created * 0.25f)
  }

  override def destroy() : Unit = {
    receiver.location.effectDb -= this
    this.receiver.cease(this)
    super.destroy()
  }

  override def update(gameTime:Float): Unit = {

  }

  override def move(gameTime:Float): Unit = {
    this.body.setGravityScale(0f)
    this.sprite.setScale(scaleX, scaleY)
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
