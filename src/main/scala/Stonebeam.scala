import com.badlogic.gdx.graphics.g2d.{Sprite, Animation, TextureRegion}
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d._

import scala.collection.mutable.ListBuffer

class Stonebeam(name:String, world:World, animationSheet:ListBuffer[TextureRegion], posX:Float, posY:Float, scaleX:Float,  scaleY:Float) extends Bullet(name:String, world:World, animationSheet:ListBuffer[TextureRegion], posX:Float, posY:Float, scaleX:Float, scaleY:Float) {
  attackAnimationRight = new Animation(0.05f, animationSheet(24),animationSheet(25),animationSheet(26),animationSheet(27))
  attackAnimationLeft  = new Animation(0.05f, animationSheet(24),animationSheet(25),animationSheet(26),animationSheet(27))
  life = 2.5f
  stone = true

  override def init(): Unit = {
    this.sprite = new Sprite(animationSheet.head)
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
        shape.setAsBox(GameUtil.pixelsToMeters(width / 2.2f), GameUtil.pixelsToMeters(height / 4.2f))
        f.friction = 0f; f})

    body.setUserData(sprite)
    fixture.setUserData(this)

    GameLoader.bulletDb += this
    GameLoader.thingDb += this
  }

  override def move(gameTime:Float): Unit = {
    body.setGravityScale(0.0f)
    val vel:Vector2 = body.getLinearVelocity
    if(mov_h.equalsIgnoreCase("R")) {
      vel.x += 0.1f
      sprite.setRegion(attackAnimationRight.getKeyFrame(gameTime, true))
    } else {
      vel.x -= 0.1f
      sprite.setRegion(attackAnimationLeft.getKeyFrame(gameTime, true))
    }
    vel.y = 0f
    if(!moving) {
      vel.x = body.getLinearVelocity.x * 0.7f
      this.body.setGravityScale(0f)
    }
    body.setLinearVelocity(vel)
    if(life + created < GameLoader.gameTime) {
      destroy()

    }
  }
}
