import com.badlogic.gdx.graphics.g2d.{TextureRegion, Animation, Sprite}
import com.badlogic.gdx.physics.box2d.{World, PolygonShape, FixtureDef, BodyDef}

import scala.collection.mutable.ListBuffer

class Burn(name:String, world:World, attacker:Thing, receiver:Thing, posX:Float, posY:Float,override val scaleX:Float, override val scaleY:Float) extends Effect(name:String, world:World, attacker:Thing, receiver:Thing, posX:Float, posY:Float,scaleX:Float, scaleY:Float) {
  def animationSheet:ListBuffer[TextureRegion] = Effect.sheetTextures
  def effectAnimation = new Animation(0.15f, animationSheet(0),animationSheet(1),animationSheet(2),animationSheet(3))

  override def move(gameTime:Float): Unit = {
    sprite.setRegion(effectAnimation.getKeyFrame(gameTime, true))
    super.move(gameTime)
  }

  override def init(): Unit = {
    this.sprite = new Sprite(Effect.sheetTextures.head)
    this.sprite.setScale(scaleX, scaleY)

    var width = sprite.getWidth * scaleX
    var height = sprite.getHeight * scaleY

    this.body = world
      .createBody(
        {val b: BodyDef = new BodyDef()
          b.`type` = BodyDef.BodyType.DynamicBody
          b.fixedRotation = true
          b.position.set(GameUtil.pixelsToMeters(posX), GameUtil.pixelsToMeters(posY))
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

    super.init()
  }
}
