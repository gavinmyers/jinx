import com.badlogic.gdx.graphics.g2d.{Sprite, TextureRegion}
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d._
import net.dermetfan.gdx.graphics.g2d.Box2DSprite

class Thing(world:World, texture:TextureRegion, bodyType:BodyDef.BodyType, posX:Float, posY:Float) {

  var sprite:Box2DSprite = _
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
  shape.setAsBox(sprite.getHeight() / 2, sprite.getWidth() / 2)


  body = world.createBody(bodyDef)
  body.createFixture(fixtureDef)
  body.setUserData(sprite)

  def moveRight(): Unit = {
    var vel:Vector2 = body.getLinearVelocity()
    vel.x += 5
    body.setLinearVelocity(vel);
  }

  def moveLeft(): Unit = {
    var vel:Vector2 = body.getLinearVelocity()
    vel.x -= 5
    body.setLinearVelocity(vel);
  }

  def moveUp(): Unit = {
    var vel:Vector2 = body.getLinearVelocity()
    vel.y += 5
    body.setLinearVelocity(vel);
  }

  def moveDown(): Unit = {
    var vel:Vector2 = body.getLinearVelocity()
    vel.y -= 5
    body.setLinearVelocity(vel);
  }
}
