import com.badlogic.gdx.graphics.g2d.{Sprite, TextureRegion}
import com.badlogic.gdx.physics.box2d._

class Corpse(name:String, room:Room, texture:TextureRegion, bodyType:BodyDef.BodyType, posX:Float, posY:Float, scaleX:Float, scaleY:Float)
  extends Thing(room:Room) {


  var bodyDef = new BodyDef()

  bodyDef.`type` = BodyDef.BodyType.DynamicBody
  bodyDef.fixedRotation = true
  bodyDef.position.set(GameUtil.pixelsToMeters(posX), GameUtil.pixelsToMeters(posY))

  var fixtureDef = new FixtureDef()

  var shape = new CircleShape()

  fixtureDef.shape = shape
  fixtureDef.isSensor = false
  fixtureDef.filter.categoryBits = Thing.tool
  fixtureDef.filter.maskBits = Thing.floor
  fixtureDef.friction = 10f

  sprite = new Sprite(texture)
  sprite.setScale(scaleX, scaleY)
  sprite.setAlpha(1f)
  var width = sprite.getWidth * scaleX
  var height = sprite.getHeight * scaleY

  shape.setRadius(GameUtil.pixelsToMeters(height / 3f))

  body = room.world.createBody(bodyDef)

  var fixture = body.createFixture(fixtureDef)
  body.setUserData(sprite)
  fixture.setUserData(this)

  room.thingDb += this

  override def contact(thing:Thing) : Unit = {

  }


}