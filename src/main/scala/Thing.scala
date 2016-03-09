import com.badlogic.gdx.graphics.g2d.{Batch, Sprite, TextureRegion}
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d._
import net.dermetfan.gdx.graphics.g2d.Box2DSprite

class Thing(world:World, texture:TextureRegion, bodyType:BodyDef.BodyType, posX:Float, posY:Float) {

  var sprite:Sprite = _
  var fixtureBottom:FixtureDef = _
  var fixtureTop:FixtureDef = _
  var fixtureRight:FixtureDef = _
  var fixtureLeft:FixtureDef = _
  var bodyDef:BodyDef = _
  var body:Body = _
  var shape:PolygonShape = _
  var fixtureDef:FixtureDef = _
  var fixture:Fixture = _

  bodyDef = new BodyDef()

  bodyDef.`type` = bodyType
  bodyDef.fixedRotation = true
  bodyDef.position.set(posX, posY)

  fixtureDef = new FixtureDef()

  shape = new PolygonShape()

  fixtureDef.shape = shape
  fixtureDef.density = 9
  fixtureDef.friction = .4f



  sprite = new Sprite(texture)
  shape.setAsBox(sprite.getHeight / 2, sprite.getWidth / 2)

  fixtureBottom = new FixtureDef
  fixtureBottom.isSensor = true
  fixtureBottom.shape = new PolygonShape()
  fixtureBottom.shape.asInstanceOf[PolygonShape].setAsBox(sprite.getWidth / 2.2f, sprite.getHeight / 4, new Vector2(0f, -1 * sprite.getHeight / 2), 0)

  fixtureTop = new FixtureDef
  fixtureTop.isSensor = true
  fixtureTop.shape = new PolygonShape()
  fixtureTop.shape.asInstanceOf[PolygonShape].setAsBox(sprite.getWidth / 2.2f, sprite.getHeight / 4, new Vector2(0f, sprite.getHeight / 2), 0)

  fixtureRight = new FixtureDef
  fixtureRight.isSensor = true
  fixtureRight.shape = new PolygonShape()
  fixtureRight.shape.asInstanceOf[PolygonShape].setAsBox(sprite.getWidth / 4, sprite.getHeight / 2.2f, new Vector2(sprite.getWidth / 2, 0f), 0)

  fixtureLeft = new FixtureDef
  fixtureLeft.isSensor = true
  fixtureLeft.shape = new PolygonShape()
  fixtureLeft.shape.asInstanceOf[PolygonShape].setAsBox(sprite.getWidth / 4, sprite.getHeight / 2.2f, new Vector2(-1 * sprite.getWidth / 2, 0f), 0)


  body = world.createBody(bodyDef)
  body.createFixture(fixtureDef)
  body.createFixture(fixtureBottom)
  body.createFixture(fixtureTop)
  body.createFixture(fixtureRight)
  body.createFixture(fixtureLeft)
  body.setUserData(sprite)

  def draw(batch:Batch): Unit = {
    sprite.setPosition(body.getPosition.x - texture.getRegionWidth / 2,body.getPosition.y - texture.getRegionHeight / 2)
    sprite.draw(batch)
  }

  def moveRight(): Unit = {
    val vel:Vector2 = body.getLinearVelocity
    vel.x += 5
    body.setLinearVelocity(vel)
  }

  def moveLeft(): Unit = {
    val vel:Vector2 = body.getLinearVelocity
    vel.x -= 5
    body.setLinearVelocity(vel)
  }

  def moveUp(): Unit = {
    val vel:Vector2 = body.getLinearVelocity
    vel.y += 5
    body.setLinearVelocity(vel)
  }

  def moveDown(): Unit = {
    val vel:Vector2 = body.getLinearVelocity
    vel.y -= 5
    body.setLinearVelocity(vel)
  }
}
