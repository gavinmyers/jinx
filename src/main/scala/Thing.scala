import com.badlogic.gdx.graphics.g2d.{Sprite, TextureRegion}
import com.badlogic.gdx.physics.box2d._
import net.dermetfan.gdx.graphics.g2d.Box2DSprite

class Thing(world:World, texture:TextureRegion, bodyType:BodyDef.BodyType, posX:Float, posY:Float) {

  var sprite:Sprite = _
  var bodyDef:BodyDef = _
  var body:Body = _
  var shape:PolygonShape = _
  var fixtureDef:FixtureDef = _
  var fixture:Fixture = _

  bodyDef = new BodyDef()

  bodyDef.`type` = bodyType
  bodyDef.position.set(posX, posY)

  fixtureDef = new FixtureDef()

  shape = new PolygonShape()

  fixtureDef.shape = shape
  fixtureDef.density = 9
  fixtureDef.friction = .4f

  sprite = new Box2DSprite(texture)
  shape.setAsBox(sprite.getHeight(), sprite.getWidth())

  body = world.createBody(bodyDef)
  body.createFixture(fixtureDef)
  body.setUserData(sprite)

}