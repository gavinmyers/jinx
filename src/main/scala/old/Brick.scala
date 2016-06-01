package old

import com.badlogic.gdx.graphics.g2d.{Sprite, TextureRegion}
import com.badlogic.gdx.physics.box2d._

class Brick(name:String,room:Room,texture:TextureRegion, posX:Float, posY:Float)
  extends Thing(room:Room) {

  this.created = GameLoader.gameTime

  var bodyDef = new BodyDef()

  bodyDef.`type` = BodyDef.BodyType.StaticBody
  bodyDef.fixedRotation = true
  bodyDef.position.set(GameUtil.pixelsToMeters(posX), GameUtil.pixelsToMeters(posY))

  var fixtureDef = new FixtureDef()

  var shape = new PolygonShape()

  fixtureDef.shape = shape
  fixtureDef.friction = 5f
  fixtureDef.filter.categoryBits = Thing.floor
  //fixtureDef.filter.maskBits = 0x1

  sprite = new Sprite(texture)
  shape.setAsBox(GameUtil.pixelsToMeters(sprite.getHeight / 2), GameUtil.pixelsToMeters(sprite.getWidth / 2))


  body = room.world.createBody(bodyDef)

  var fixture = body.createFixture(fixtureDef)

  var f:Filter = new Filter()
  f.groupIndex = 2
  fixture.setFilterData(f)
  body.setUserData(sprite)

  body.setUserData(sprite)
  fixture.setUserData(this)
  room.groundDb += name -> this
  room.thingDb += this

  override def contact(thing:Thing) : Unit = {

  }


}

