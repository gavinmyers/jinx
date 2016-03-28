import com.badlogic.gdx.graphics.g2d.{Sprite, TextureRegion}
import com.badlogic.gdx.physics.box2d._

class Corpse(name:String, world:World, texture:TextureRegion, bodyType:BodyDef.BodyType, posX:Float, posY:Float, scaleX:Float, scaleY:Float)
  extends Thing() {


  var bodyDef = new BodyDef()

  bodyDef.`type` = bodyType
  bodyDef.fixedRotation = true
  bodyDef.position.set(GameUtil.pixelsToMeters(posX), GameUtil.pixelsToMeters(posY))

  var fixtureDef = new FixtureDef()

  var shape = new PolygonShape()

  fixtureDef.shape = shape
  fixtureDef.isSensor = true
  fixtureDef.friction = 5f

  sprite = new Sprite(texture)
  sprite.setScale(scaleX, scaleY)
  var width = sprite.getWidth * scaleX
  var height = sprite.getHeight * scaleY

  shape.setAsBox(0, 0)

  body = world.createBody(bodyDef)

  var fixture = body.createFixture(fixtureDef)

  body.setUserData(sprite)
  fixture.setUserData(this)
  GameLoader.thingDb += this

  override def contact(thing:Thing) : Unit = {

  }

  override def damage(source:Thing, amount:Integer): Unit = {

  }
}