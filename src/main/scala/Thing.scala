import box2dLight.{PointLight, PositionalLight, RayHandler}
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.{Animation, Batch, Sprite, TextureRegion}
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d._
import net.dermetfan.gdx.graphics.g2d.Box2DSprite
import scala.collection.JavaConversions._
import scala.collection.mutable.ListBuffer

class Thing(item_name:String, world:World, texture:TextureRegion, bodyType:BodyDef.BodyType, posX:Float, posY:Float) {

  lazy val light:PositionalLight = new PointLight(GameLoader.handler, 1024, new Color(1f, 1f, 1f, 0.2f), 256, 0, 0);

  var name:String = item_name

  var created:Float = GameLoader.gameTime

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

  var direction:String = ""


  GameLoader.thingDb += this



  def draw(batch:Batch): Unit = {
    sprite.setPosition(body.getPosition.x - texture.getRegionWidth / 2,body.getPosition.y - texture.getRegionHeight / 2)
    sprite.draw(batch)

  }

  def update(batch:Batch): Unit = {
  }

  def destroy() : Unit = {
    GameLoader.thingDb -= this
  }

  def contact(thing:Thing) : Unit = {
    //do nothing
  }

  def damage(source:Thing, amount:Integer): Unit = {

  }
}

class Brick(item_name:String, world:World, texture:TextureRegion, bodyType:BodyDef.BodyType, posX:Float, posY:Float)
  extends Thing(item_name:String, world:World, texture:TextureRegion, bodyType:BodyDef.BodyType, posX:Float, posY:Float) {


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
  fixture.setUserData(this)
  GameLoader.groundDb += name -> this

  override def contact(thing:Thing) : Unit = {

  }

  override def damage(source:Thing, amount:Integer): Unit = {

  }
}

class Being(item_name:String,world:World, texture:TextureRegion, bodyType:BodyDef.BodyType, posX:Float, posY:Float)
  extends Thing(item_name:String,world:World, texture:TextureRegion, bodyType:BodyDef.BodyType, posX:Float, posY:Float) {

  var life = 10
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

  walkRightAnimation = new Animation(0.15f, GameLoader.player.get(4),GameLoader.player.get(5))
  walkLeftAnimation = new Animation(0.15f, GameLoader.player.get(6),GameLoader.player.get(7))

  GameLoader.handler.setAmbientLight(0.2f, 0.2f, 0.2f, 0.6f)
  light.attachToBody(body, 0, 0)
  light.setIgnoreAttachedBody(true)


  fixture.setUserData(this)

  GameLoader.monsterDb += name -> this

  def moveRight(gameTime:Float): Unit = {
    direction = "R"
    val vel:Vector2 = body.getLinearVelocity
    vel.x += 5
    body.setLinearVelocity(vel)
    sprite.setRegion(walkRightAnimation.getKeyFrame(gameTime, true))
  }

  def moveLeft(gameTime:Float): Unit = {
    direction = "L"
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

  var lastAttack:Float= 0
  var cooldown:Float = 0.3f
  def attack(gameTime:Float): Unit = {

    if(lastAttack + cooldown > gameTime) {
      return
    }
    lastAttack = gameTime
    var x = sprite.getX
    if(direction.equalsIgnoreCase("R")) {
      x = sprite.getX + (sprite.getWidth)
    }
    var y = sprite.getY + (sprite.getHeight / 2)

    var b:Bullet = new Bullet(name+"_bullet_"+Math.random(),GameLoader.world, GameLoader.player(8), BodyDef.BodyType.DynamicBody, x, y)
    b.direction = direction
    b.attacker = this

  }

  def moveDown(gameTime:Float): Unit = {
    val vel:Vector2 = body.getLinearVelocity
    vel.y -= 5
    body.setLinearVelocity(vel)
  }

  override def damage(source:Thing, amount:Integer): Unit = {
    life -= amount
    if(life < 1)
      destroy()
  }

  override def destroy() : Unit = {
    light.setActive(false)
    GameLoader.world.destroyBody(body)
    GameLoader.thingDb -= this
    GameLoader.monsterDb.remove(name)
  }
}



class Bullet(item_name:String, world:World, texture:TextureRegion, bodyType:BodyDef.BodyType, posX:Float, posY:Float)
  extends Thing(item_name:String, world:World, texture:TextureRegion, bodyType:BodyDef.BodyType, posX:Float, posY:Float) {

  bodyDef = new BodyDef()

  bodyDef.`type` = bodyType
  bodyDef.fixedRotation = true
  bodyDef.position.set(posX, posY)
  bodyDef.bullet = true

  fixtureDef = new FixtureDef()

  shape = new PolygonShape()

  fixtureDef.shape = shape
  fixtureDef.density = Float.MinValue
  fixtureDef.isSensor = true
  fixtureDef.friction = 0f

  var life:Float = 0.15f
  var attacker:Being = null

  sprite = new Sprite(texture)
  shape.asInstanceOf[PolygonShape].setAsBox(sprite.getHeight / 8, sprite.getWidth / 8)

  body = world.createBody(bodyDef)
  body.setBullet(true)

  fixture = body.createFixture(fixtureDef)

  body.setUserData(sprite)

  fixture.setUserData(this)
  GameLoader.bulletDb += this

  override def destroy() : Unit = {
    GameLoader.world.destroyBody(body)
    GameLoader.thingDb -= this
    GameLoader.bulletDb -= this
  }

  var contactList:ListBuffer[Thing] = ListBuffer()
  override def contact(thing:Thing) : Unit = {
    if(thing.isInstanceOf[Brick]) {
      this.life = 0
    } else if(thing.isInstanceOf[Being] && contactList.contains(thing) == false && thing != attacker) {
      contactList += thing
      thing.damage(this, 1)
    }
  }

}