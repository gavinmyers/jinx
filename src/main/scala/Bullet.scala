import com.badlogic.gdx.graphics.g2d.{Batch, Animation, Sprite, TextureRegion}
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.{PolygonShape, FixtureDef, BodyDef, World}
import scala.collection.JavaConversions._
import scala.collection.mutable.ListBuffer

class Bullet(item_name:String, world:World, as:ListBuffer[TextureRegion], posX:Float, posY:Float)
  extends Thing() {

  this.name = item_name

  var life = 0.3
  var attacker:Thing = null
  def animationSheet:ListBuffer[TextureRegion] = as

  bodyDef = new BodyDef()

  bodyDef.`type` = BodyDef.BodyType.DynamicBody
  bodyDef.fixedRotation = true
  bodyDef.position.set(GameUtil.pixelsToMeters(posX), GameUtil.pixelsToMeters(posY))

  fixtureDef = new FixtureDef()

  shape = new PolygonShape()

  fixtureDef.shape = shape
  fixtureDef.friction = 0.1f
  fixtureDef.density = 0
  fixtureDef.isSensor = true

  sprite = new Sprite(animationSheet(0))

  var attackAnimationRight:Animation = new Animation(0.15f, animationSheet(0),animationSheet(1),animationSheet(2))
  var attackAnimationLeft:Animation = new Animation(0.15f, animationSheet(8),animationSheet(9),animationSheet(10))

  shape.asInstanceOf[PolygonShape].setAsBox(GameUtil.pixelsToMeters(sprite.getHeight / 2), GameUtil.pixelsToMeters(sprite.getWidth / 2))


  body = world.createBody(bodyDef)

  fixture = body.createFixture(fixtureDef)

  body.setUserData(sprite)
  fixture.setUserData(this)

  GameLoader.bulletDb += this
  GameLoader.thingDb += this

  override def destroy() : Unit = {
    GameLoader.world.destroyBody(body)
    GameLoader.thingDb -= this
    GameLoader.bulletDb -= this
  }

  var contactList:ListBuffer[Thing] = ListBuffer()
  override def contact(thing:Thing) : Unit = {
    if(thing.isInstanceOf[Brick]) {
      //this.life = this.life
    } else if(thing.isInstanceOf[Being] && contactList.contains(thing) == false && thing != attacker) {

      contactList += thing
      thing.damage(this, 1)
    }
  }

  override def move(gameTime:Float): Unit = {
    val vel:Vector2 = body.getLinearVelocity
    if(mov_h.equalsIgnoreCase("R")) {
      vel.x = attacker.body.getLinearVelocity.x + 0.2f
      sprite.setRegion(attackAnimationRight.getKeyFrame(gameTime, true))
    } else {
      vel.x = attacker.body.getLinearVelocity.x - 0.2f
      sprite.setRegion(attackAnimationLeft.getKeyFrame(gameTime, true))
    }
    vel.y = attacker.body.getLinearVelocity.y + 1.1f
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