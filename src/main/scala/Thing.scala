import com.badlogic.gdx.graphics.g2d.{Animation, Batch, Sprite, TextureRegion}
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d._
import net.dermetfan.gdx.graphics.g2d.Box2DSprite
import scala.collection.JavaConversions._

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
  var shape:Shape = _



  GameLoader.thingDb = GameLoader.thingDb :+ this


  def draw(batch:Batch): Unit = {
    sprite.setPosition(body.getPosition.x - texture.getRegionWidth / 2,body.getPosition.y - texture.getRegionHeight / 2)
    sprite.draw(batch)
  }

  def update(batch:Batch): Unit = {
  }
}

class Brick(world:World, texture:TextureRegion, bodyType:BodyDef.BodyType, posX:Float, posY:Float)
  extends Thing(world:World, texture:TextureRegion, bodyType:BodyDef.BodyType, posX:Float, posY:Float) {


  bodyDef = new BodyDef()

  bodyDef.`type` = bodyType
  bodyDef.fixedRotation = true
  bodyDef.position.set(posX, posY)

  fixtureDef = new FixtureDef()

  shape = new PolygonShape()

  fixtureDef.shape = shape
  fixtureDef.density = Float.MaxValue
  fixtureDef.friction = 1f



  sprite = new Sprite(texture)
  shape.asInstanceOf[PolygonShape].setAsBox(sprite.getHeight / 2, sprite.getWidth / 2)


  body = world.createBody(bodyDef)

  fixture = body.createFixture(fixtureDef)

  body.setUserData(sprite)


}

class Being(world:World, texture:TextureRegion, bodyType:BodyDef.BodyType, posX:Float, posY:Float)
  extends Thing(world:World, texture:TextureRegion, bodyType:BodyDef.BodyType, posX:Float, posY:Float) {


  var walkRightAnimation:Animation = _
  var walkLeftAnimation:Animation = _

  bodyDef = new BodyDef()

  bodyDef.`type` = bodyType
  bodyDef.fixedRotation = true
  bodyDef.position.set(posX, posY)

  fixtureDef = new FixtureDef()

  shape = new PolygonShape()

  fixtureDef.shape = shape
  fixtureDef.density = 10f
  fixtureDef.friction = 2.5f



  sprite = new Sprite(texture)
  //shape.setRadius(sprite.getHeight / 2)

  shape.asInstanceOf[PolygonShape].setAsBox(sprite.getHeight / 2.1f, sprite.getWidth / 2.1f)

  fixtureDefBottom = new FixtureDef
  fixtureDefBottom.isSensor = true
  fixtureDefBottom.shape = new PolygonShape()
  fixtureDefBottom.shape.asInstanceOf[PolygonShape].setAsBox(sprite.getWidth / 3f, sprite.getHeight / 4.5f, new Vector2(0f, -1 * sprite.getHeight / 2.5f), 0)

  fixtureDefTop = new FixtureDef
  fixtureDefTop.isSensor = true
  fixtureDefTop.shape = new PolygonShape()
  fixtureDefTop.shape.asInstanceOf[PolygonShape].setAsBox(sprite.getWidth / 3f, sprite.getHeight / 4.5f, new Vector2(0f, sprite.getHeight / 2.5f), 0)

  fixtureDefRight = new FixtureDef
  fixtureDefRight.isSensor = true
  fixtureDefRight.shape = new PolygonShape()
  fixtureDefRight.shape.asInstanceOf[PolygonShape].setAsBox(sprite.getWidth / 4.5f, sprite.getHeight / 3f, new Vector2(sprite.getWidth / 2.5f, 0f), 0)

  fixtureDefLeft = new FixtureDef
  fixtureDefLeft.isSensor = true
  fixtureDefLeft.shape = new PolygonShape()
  fixtureDefLeft.shape.asInstanceOf[PolygonShape].setAsBox(sprite.getWidth / 4.5f, sprite.getHeight / 3f, new Vector2(-1 * sprite.getWidth / 2.5f, 0f), 0)



  body = world.createBody(bodyDef)

  fixture = body.createFixture(fixtureDef)
  fixtureBottom = body.createFixture(fixtureDefBottom)
  fixtureTop = body.createFixture(fixtureDefTop)
  fixtureRight = body.createFixture(fixtureDefRight)
  fixtureLeft = body.createFixture(fixtureDefLeft)

  body.setUserData(sprite)

  walkRightAnimation = new Animation(0.15f, GameLoader.player(4),GameLoader.player(5))
  walkLeftAnimation = new Animation(0.15f, GameLoader.player(6),GameLoader.player(7))

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

  def canJump():Boolean = {
    for(contact:Contact <- GameLoader.world.getContactList()) {
      if(!contact.getFixtureB.isSensor && contact.getFixtureA == fixtureBottom) {
        return true
      }
      if(!contact.getFixtureA.isSensor && contact.getFixtureB == fixtureBottom) {
        return true
      }
    }
    return false
  }

  var jumpTime:Float = 0
  var jumpRelease:Boolean = false
  var jumpGravity:Float = 0
  override def update(batch:Batch) = {
    if(canJump() == false) {
      println(jumpTime)
      jumpTime += 1
      if(jumpRelease || jumpTime > 50) {
        jumpGravity += 0.15f
      }
      this.fixture.setFriction(0f)
      val vel:Vector2 = body.getLinearVelocity
      vel.y -= jumpGravity
      body.setLinearVelocity(vel)
    } else {
      jumpTime = 0f
      jumpGravity = 0f
      if(this.fixture.getFriction < 2.5f) {
        this.fixture.setFriction(this.fixture.getFriction + 0.1f)
      }
    }
    jumpRelease = true
  }

  def moveUp(gameTime:Float): Unit = {
    jumpRelease = false
    if(canJump()) {
      val vel: Vector2 = body.getLinearVelocity
      vel.y += 150
      body.setLinearVelocity(vel)
    }
  }

  def moveDown(gameTime:Float): Unit = {
    val vel:Vector2 = body.getLinearVelocity
    vel.y -= 5
    body.setLinearVelocity(vel)
  }
}