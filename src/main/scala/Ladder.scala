import com.badlogic.gdx.graphics.g2d.{Sprite, TextureRegion}
import com.badlogic.gdx.physics.box2d._

class Ladder(name:String, world:World, texture:TextureRegion, bodyType:BodyDef.BodyType, posX:Float, posY:Float)
  extends Thing() {

  var fixtureDef:FixtureDef = _
  var fixture:Fixture = _
  var bodyDef:BodyDef = _
  var shape:Shape = _
  bodyDef = new BodyDef()

  bodyDef.`type` = bodyType
  bodyDef.fixedRotation = true
  bodyDef.position.set(GameUtil.pixelsToMeters(posX), GameUtil.pixelsToMeters(posY))

  fixtureDef = new FixtureDef()

  shape = new PolygonShape()

  fixtureDef.shape = shape
  fixtureDef.isSensor = true
  fixtureDef.friction = 5f


  sprite = new Sprite(texture)
  shape.asInstanceOf[PolygonShape].setAsBox(GameUtil.pixelsToMeters(sprite.getWidth / 4), GameUtil.pixelsToMeters(sprite.getHeight / 2))


  body = world.createBody(bodyDef)

  fixture = body.createFixture(fixtureDef)



  body.setUserData(sprite)
  fixture.setUserData(this)
  GameLoader.thingDb += this

  override def contact(thing:Thing) : Unit = {

  }

  override def damage(source:Thing, amount:Float): Unit = {

  }
}
