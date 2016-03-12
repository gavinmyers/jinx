import com.badlogic.gdx.graphics.g2d.{Animation, Batch, Sprite, TextureRegion}
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d._
import net.dermetfan.gdx.graphics.g2d.Box2DSprite

class Thing(world:World, texture:TextureRegion, bodyType:BodyDef.BodyType, posX:Float, posY:Float) {

  var sprite:Sprite = _
  var fixtureDefBottom:FixtureDef = _
  var fixtureDefTop:FixtureDef = _
  var fixtureDefRight:FixtureDef = _
  var fixtureDefLeft:FixtureDef = _
  var fixtureDef:FixtureDef = _

  var fixtureBottom:Fixture = _
  var fixtureTop:Fixture = _
  var fixtureRight:Fixture = _
  var fixtureLeft:Fixture = _
  var fixture:Fixture = _


  var bodyDef:BodyDef = _
  var body:Body = _
  var shape:PolygonShape = _

  var walkRightAnimation:Animation = _
  var walkLeftAnimation:Animation = _

  bodyDef = new BodyDef()

  bodyDef.`type` = bodyType
  bodyDef.fixedRotation = true
  bodyDef.position.set(posX, posY)

  fixtureDef = new FixtureDef()

  shape = new PolygonShape()

  fixtureDef.shape = shape
  fixtureDef.density = 15
  fixtureDef.friction = .4f



  sprite = new Sprite(texture)
  shape.setAsBox(sprite.getHeight / 2, sprite.getWidth / 2)

  fixtureDefBottom = new FixtureDef
  fixtureDefBottom.isSensor = true
  fixtureDefBottom.shape = new PolygonShape()
  fixtureDefBottom.shape.asInstanceOf[PolygonShape].setAsBox(sprite.getWidth / 2.2f, sprite.getHeight / 4, new Vector2(0f, -1 * sprite.getHeight / 2), 0)

  fixtureDefTop = new FixtureDef
  fixtureDefTop.isSensor = true
  fixtureDefTop.shape = new PolygonShape()
  fixtureDefTop.shape.asInstanceOf[PolygonShape].setAsBox(sprite.getWidth / 2.2f, sprite.getHeight / 4, new Vector2(0f, sprite.getHeight / 2), 0)

  fixtureDefRight = new FixtureDef
  fixtureDefRight.isSensor = true
  fixtureDefRight.shape = new PolygonShape()
  fixtureDefRight.shape.asInstanceOf[PolygonShape].setAsBox(sprite.getWidth / 4, sprite.getHeight / 2.2f, new Vector2(sprite.getWidth / 2, 0f), 0)

  fixtureDefLeft = new FixtureDef
  fixtureDefLeft.isSensor = true
  fixtureDefLeft.shape = new PolygonShape()
  fixtureDefLeft.shape.asInstanceOf[PolygonShape].setAsBox(sprite.getWidth / 4, sprite.getHeight / 2.2f, new Vector2(-1 * sprite.getWidth / 2, 0f), 0)


  body = world.createBody(bodyDef)

  fixture = body.createFixture(fixtureDef)
  fixtureBottom = body.createFixture(fixtureDefBottom)
  fixtureTop = body.createFixture(fixtureDefTop)
  fixtureRight = body.createFixture(fixtureDefRight)
  fixtureLeft = body.createFixture(fixtureDefLeft)

  body.setUserData(sprite)

  walkRightAnimation = new Animation(0.15f, GameLoader.player(4),GameLoader.player(5))
  walkLeftAnimation = new Animation(0.15f, GameLoader.player(6),GameLoader.player(7))


  GameLoader.thingDb = GameLoader.thingDb :+ this


  def draw(batch:Batch): Unit = {
    sprite.setPosition(body.getPosition.x - texture.getRegionWidth / 2,body.getPosition.y - texture.getRegionHeight / 2)
    sprite.draw(batch)
  }

  def moveRight(gameTime:Float): Unit = {
    val vel:Vector2 = body.getLinearVelocity
    vel.x += 5
    body.setLinearVelocity(vel)
    sprite.setRegion(walkRightAnimation.getKeyFrame(gameTime, true))
  }

  def moveLeft(gameTime:Float): Unit = {
    sprite.setRegion(walkLeftAnimation.getKeyFrame(gameTime, true))
    val vel:Vector2 = body.getLinearVelocity
    vel.x -= 5
    body.setLinearVelocity(vel)
  }

  def moveUp(gameTime:Float): Unit = {
    val vel:Vector2 = body.getLinearVelocity
    vel.y += 15
    body.setLinearVelocity(vel)
  }

  def moveDown(gameTime:Float): Unit = {
    val vel:Vector2 = body.getLinearVelocity
    vel.y -= 5
    body.setLinearVelocity(vel)
  }
}
