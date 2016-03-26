import box2dLight.{PointLight, PositionalLight, RayHandler}
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.{Animation, Batch, Sprite, TextureRegion}
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d._
import net.dermetfan.gdx.graphics.g2d.Box2DSprite
import scala.collection.JavaConversions._
import scala.collection.mutable.ListBuffer

class Thing(item_name:String, world:World, texture:TextureRegion, bodyType:BodyDef.BodyType, posX:Float, posY:Float) {

  lazy val light:PositionalLight = new PointLight(GameLoader.handler, 1024, new Color(1f, 1f, 1f, 0.2f), 12, 0, 0);

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
    sprite.setPosition(GameLoader.metersToPixels(body.getPosition().x) - sprite.getWidth()/2 , GameLoader.metersToPixels(body.getPosition().y) - sprite.getHeight()/2 )
    //sprite.setPosition(body.getPosition.x - texture.getRegionWidth / 2,body.getPosition.y - texture.getRegionHeight / 2)
    //sprite.setScale(0.75f, 0.75f)
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
  bodyDef.position.set(GameLoader.pixelsToMeters(posX), GameLoader.pixelsToMeters(posY))

  fixtureDef = new FixtureDef()

  shape = new PolygonShape()

  fixtureDef.shape = shape
  fixtureDef.friction = 5f


  sprite = new Sprite(texture)
  shape.asInstanceOf[PolygonShape].setAsBox(GameLoader.pixelsToMeters(sprite.getHeight / 2), GameLoader.pixelsToMeters(sprite.getWidth / 2))


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
  bodyDef.position.set(GameLoader.pixelsToMeters(posX), GameLoader.pixelsToMeters(posY))

  fixtureDef = new FixtureDef()

//  shape = new PolygonShape()
  shape = new CircleShape()
  fixtureDef.shape = shape
  fixtureDef.friction = 0f



  sprite = new Sprite(texture)

  shape.setRadius(GameLoader.pixelsToMeters(sprite.getHeight / 2.2f))
//  shape.asInstanceOf[PolygonShape].setAsBox(GameLoader.pixelsToMeters(sprite.getHeight / 2.2f), GameLoader.pixelsToMeters(sprite.getWidth / 2.2f))


  def w1 = GameLoader.pixelsToMeters(sprite.getWidth / 3f)
  def h1 = GameLoader.pixelsToMeters(sprite.getHeight / 3f)
  def w2 = GameLoader.pixelsToMeters(sprite.getWidth / 4.5f)
  def h2 = GameLoader.pixelsToMeters(sprite.getHeight / 4.5f)
  fixtureDefBottom = new FixtureDef
  fixtureDefBottom.density = 0f
  fixtureDefBottom.isSensor = true
  fixtureDefBottom.shape = new PolygonShape()
  fixtureDefBottom.shape.asInstanceOf[PolygonShape].setAsBox(w1, h2, new Vector2(0f, GameLoader.pixelsToMeters(-1 * sprite.getHeight / 2.5f)), 0)

  fixtureDefTop = new FixtureDef
  fixtureDefTop.isSensor = true
  fixtureDefTop.density = 0f
  fixtureDefTop.shape = new PolygonShape()
  fixtureDefTop.shape.asInstanceOf[PolygonShape].setAsBox(w1, h2, new Vector2(0f, GameLoader.pixelsToMeters(sprite.getHeight / 2.5f)), 0)

  fixtureDefRight = new FixtureDef
  fixtureDefRight.isSensor = true
  fixtureDefRight.density = 0f
  fixtureDefRight.shape = new PolygonShape()
  fixtureDefRight.shape.asInstanceOf[PolygonShape].setAsBox(w2, h1, new Vector2(GameLoader.pixelsToMeters(sprite.getWidth / 2.5f), 0f), 0)

  fixtureDefLeft = new FixtureDef
  fixtureDefLeft.isSensor = true
  fixtureDefLeft.density = 0f
  fixtureDefLeft.shape = new PolygonShape()
  fixtureDefLeft.shape.asInstanceOf[PolygonShape].setAsBox(w2, h1, new Vector2(GameLoader.pixelsToMeters(-1 * sprite.getWidth / 2.5f), 0f), 0)

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


  def fall(gameTime:Float): Unit = {
    jumping = false
    body.setLinearVelocity(body.getLinearVelocity.x, body.getLinearVelocity.y * 0.9f)
  }

  def stop(gameTime:Float): Unit = {
    direction = ""
    moveSpeed = 0f
    body.setLinearVelocity(body.getLinearVelocity.x * 0.9f, body.getLinearVelocity.y)
    sprite.setRegion(walkRightAnimation.getKeyFrame(gameTime, true))
  }

  var moveSpeed:Float = 0f
  def move(gameTime:Float): Unit = {
    if(direction == "" && jumping == false) return stop(gameTime)

    moveSpeed += 0.5f
    if(direction == "R") {
      if(body.getLinearVelocity.x < 10f)
        body.applyForceToCenter(15f, 0f, true)
      sprite.setRegion(walkRightAnimation.getKeyFrame(gameTime, true))
    }
    if(direction == "L") {
      if(body.getLinearVelocity.x > -10f)
        body.applyForceToCenter(-15f, 0f, true)
      sprite.setRegion(walkLeftAnimation.getKeyFrame(gameTime, true))
    }
    if(jumping) {
      if(body.getLinearVelocity.y < 15f)
        body.applyForceToCenter(0f, 250f, true)
    }
  }

  def moveRight(gameTime:Float): Unit = {
    direction = "R"

  }

  def moveLeft(gameTime:Float): Unit = {
    direction = "L"
  }

  var jumping:Boolean = false
  var jumpPower:Float = 0f
  var jumpCooldown:Float = 0.3f
  var lastJump:Float = 0
  def moveUp(gameTime:Float): Unit = {
    jumping = true
    if(lastJump + jumpCooldown > gameTime) {
      return
    }
    lastJump = gameTime

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

  override def damage(source:Thing, amount:Integer): Unit = {
    life -= amount
    if(life < 1)
      destroy()
  }

  override def draw(batch:Batch): Unit = {
    move(GameLoader.gameTime)
    super.draw(batch)
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

  var life = 0.3
  var attacker:Thing = null

  bodyDef = new BodyDef()

  bodyDef.`type` = bodyType
  bodyDef.fixedRotation = true
  bodyDef.position.set(GameLoader.pixelsToMeters(posX), GameLoader.pixelsToMeters(posY))

  fixtureDef = new FixtureDef()

  shape = new PolygonShape()

  fixtureDef.shape = shape
  fixtureDef.friction = 0.1f
  fixtureDef.density = 0
  fixtureDef.isSensor = true

  sprite = new Sprite(texture)

  shape.asInstanceOf[PolygonShape].setAsBox(GameLoader.pixelsToMeters(sprite.getHeight / 2), GameLoader.pixelsToMeters(sprite.getWidth / 2))


  body = world.createBody(bodyDef)

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
      //this.life = this.life
    } else if(thing.isInstanceOf[Being] && contactList.contains(thing) == false && thing != attacker) {
      contactList += thing
      thing.damage(this, 1)
    }
  }

}