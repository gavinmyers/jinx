import com.badlogic.gdx.graphics.g2d.{Batch, Animation, Sprite, TextureRegion}
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d._
import scala.collection.JavaConversions._
import scala.collection.mutable.ListBuffer

class Bullet(name:String, world:World, animationSheet:ListBuffer[TextureRegion], posX:Float, posY:Float,val scaleX:Float, val scaleY:Float)
  extends Thing() {
  this.created = GameLoader.gameTime
  var life = 0.3

  var ice:Boolean = false
  var fire:Boolean = false
  var acid:Boolean = false
  var stone:Boolean = false
  var poison:Boolean = false

  var effects

  var damage:Float = 1

  var fixture: Fixture = _
  var attacker:Thing = _
  var attackAnimationRight:Animation = new Animation(0.15f, animationSheet(16),animationSheet(17),animationSheet(18),animationSheet(19),animationSheet(20),animationSheet(21),animationSheet(22),animationSheet(23))
  var attackAnimationLeft:Animation = new Animation(0.15f, animationSheet(16),animationSheet(17),animationSheet(18),animationSheet(19),animationSheet(20),animationSheet(21),animationSheet(22),animationSheet(23))

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
        var shape:Shape = new CircleShape()
        f.isSensor = true
        f.shape = shape
        shape.setRadius(GameUtil.pixelsToMeters(height / 2.2f))
        f.friction = 0f; f})

    body.setUserData(sprite)
    fixture.setUserData(this)

    GameLoader.bulletDb += this
    GameLoader.thingDb += this
  }

  override def destroy() : Unit = {
    GameLoader.bulletDb -= this
    super.destroy()
  }

  var contactList:ListBuffer[Thing] = ListBuffer()
  override def contact(thing:Thing) : Unit = {
    if(thing == null) {
    } else thing match {
      case _: Brick =>
        moving = false
      case _: Being if !contactList.contains(thing) && thing != attacker =>
        contactList += thing
        thing.damage(this, damage)
      case _ =>
    }
  }

  var moving:Boolean = true
  override def move(gameTime:Float): Unit = {

    val vel:Vector2 = body.getLinearVelocity
    if(mov_h.equalsIgnoreCase("R")) {
      vel.x = attacker.body.getLinearVelocity.x + 0.2f
      this.sprite.setRegion(attackAnimationRight.getKeyFrame(gameTime, true))
    } else {
      vel.x = attacker.body.getLinearVelocity.x - 0.2f
      this.sprite.setRegion(attackAnimationLeft.getKeyFrame(gameTime, true))
    }
    vel.y = attacker.body.getLinearVelocity.y + 1.1f
    if(!moving) {
      vel.x = attacker.body.getLinearVelocity.x * 0.99f
      vel.y = attacker.body.getLinearVelocity.y * 0.99f
      this.body.setGravityScale(0f)
    }
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