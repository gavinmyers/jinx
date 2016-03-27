import box2dLight.{PointLight, PositionalLight}
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.{Batch, Animation, Sprite, TextureRegion}
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d._
import scala.collection.JavaConversions._
import scala.collection.mutable.ListBuffer

class Being(item_name:String,world:World, as:ListBuffer[TextureRegion], posX:Float, posY:Float)
  extends Thing() {
  lazy val light:PositionalLight = new PointLight(GameLoader.handler, 32, new Color(1f, 1f, 1f, 0.8f), 24, 0, 0);

  name = item_name

  def animationSheet:ListBuffer[TextureRegion] = as

  var bulletSheet:ListBuffer[TextureRegion] = _

  var life = 10

  bodyDef = new BodyDef()

  bodyDef.`type` = BodyDef.BodyType.DynamicBody
  bodyDef.fixedRotation = true
  bodyDef.position.set(GameUtil.pixelsToMeters(posX), GameUtil.pixelsToMeters(posY))

  fixtureDef = new FixtureDef()

  //shape = new PolygonShape()
  shape = new CircleShape()
  fixtureDef.shape = shape
  fixtureDef.friction = 0f



  sprite = new Sprite(animationSheet.get(0))



  shape.setRadius(GameUtil.pixelsToMeters(sprite.getHeight / 2.2f))
  //shape.asInstanceOf[PolygonShape].setAsBox(GameUtil.pixelsToMeters(sprite.getHeight / 2.2f), GameUtil.pixelsToMeters(sprite.getWidth / 2.2f))


  def w1 = GameUtil.pixelsToMeters(sprite.getWidth / 3f)
  def h1 = GameUtil.pixelsToMeters(sprite.getHeight / 3f)
  def w2 = GameUtil.pixelsToMeters(sprite.getWidth / 4.5f)
  def h2 = GameUtil.pixelsToMeters(sprite.getHeight / 4.5f)
  fixtureDefBottom = new FixtureDef
  fixtureDefBottom.density = 0f
  fixtureDefBottom.isSensor = true
  fixtureDefBottom.shape = new PolygonShape()
  fixtureDefBottom.shape.asInstanceOf[PolygonShape].setAsBox(w1 / 2, h2, new Vector2(0f, GameUtil.pixelsToMeters(-1 * sprite.getHeight / 2.5f)), 0)

  fixtureDefTop = new FixtureDef
  fixtureDefTop.isSensor = true
  fixtureDefTop.density = 0f
  fixtureDefTop.shape = new PolygonShape()
  fixtureDefTop.shape.asInstanceOf[PolygonShape].setAsBox(w1, h2, new Vector2(0f, GameUtil.pixelsToMeters(sprite.getHeight / 2.5f)), 0)


  body = world.createBody(bodyDef)

  fixture = body.createFixture(fixtureDef)
  fixtureBottom = body.createFixture(fixtureDefBottom)
  fixtureTop = body.createFixture(fixtureDefTop)




  var walkRightAnimation:Animation = new Animation(0.15f, animationSheet.get(28),animationSheet.get(29))
  var standRightAnimation:Animation = new Animation(0.15f, animationSheet.get(2))

  var walkLeftAnimation:Animation = new Animation(0.15f, animationSheet.get(24),animationSheet.get(25))
  var standLeftAnimation:Animation = new Animation(0.15f, animationSheet.get(3))

  var climbAnimation:Animation = new Animation(0.25f, animationSheet.get(8),animationSheet.get(9))

  var hurtAnimation:Animation = new Animation(0.25f, animationSheet.get(48))
  var deathAnimation:Animation = new Animation(0.25f, animationSheet.get(72),animationSheet.get(73),animationSheet.get(74),animationSheet.get(75))

  var attackAnimationLeft:Animation = new Animation(0.25f, animationSheet.get(40),animationSheet.get(41))
  var attackAnimationRight:Animation = new Animation(0.25f, animationSheet.get(44),animationSheet.get(45))

  light.attachToBody(body, 0, 0)
  light.setIgnoreAttachedBody(true)
  light.setContactFilter(0,2,-1)

  fixture.setUserData(this)

  GameLoader.monsterDb += name -> this
  GameLoader.thingDb += this

  def fall(gameTime:Float): Unit = {
    jumping = false
    mov_v = ""
    body.setLinearVelocity(body.getLinearVelocity.x, body.getLinearVelocity.y * 0.9f)
  }

  def stop(gameTime:Float): Unit = {
    mov_h = ""
    body.setLinearVelocity(body.getLinearVelocity.x * 0.9f, body.getLinearVelocity.y)
    if(face_h == "R" && !attacking) {
      sprite.setRegion(standRightAnimation.getKeyFrame(gameTime, true))
    } else if (face_h == "L"  && !attacking) {
      sprite.setRegion(standLeftAnimation.getKeyFrame(gameTime, true))
    }
  }
  override def update(gameTime:Float): Unit = {
    if(dieing && deathStart + deathEnd < gameTime) {
      destroy()
      return
    }
    if(lastDamage + damageCooldown < gameTime) {
      takingDamage = false
    }
    if(lastAttack + cooldown < gameTime) {
      attacking = false
    }

    body.setGravityScale(1f)
  }

  override def move(gameTime:Float): Unit = {

    if(dieing) {
      sprite.setRegion(deathAnimation.getKeyFrame(gameTime, true))

    } else if(takingDamage) {
      sprite.setRegion(hurtAnimation.getKeyFrame(gameTime, true))

    } else if(attacking) {
      if(face_h == "R") {
        sprite.setRegion(attackAnimationRight.getKeyFrame(gameTime, true))
      } else if(face_h == "L") {
        sprite.setRegion(attackAnimationLeft.getKeyFrame(gameTime, true))
      }

    } else if((mov_v == "" || canClimb == false) && mov_h == "" && jumping == false) {
      stop(gameTime)

    } else {

      if(jumping) {
        if(body.getLinearVelocity.y < 15f && lastJump + jumpMax > gameTime) {
          body.applyForceToCenter(0f, 250f, true)
        } else {
          fall(gameTime)
        }
      } else if(canClimb) {
        //body.setGravityScale(0.01f)
        if(mov_v == "U") {
          body.setLinearVelocity(body.getLinearVelocity.x * .9f, 4.5f)
          sprite.setRegion(climbAnimation.getKeyFrame(gameTime, true))
        } else if(mov_v == "D") {
          body.setLinearVelocity(body.getLinearVelocity.x * .9f, -4.5f)
          sprite.setRegion(climbAnimation.getKeyFrame(gameTime, true))
        }

      }

      if(mov_h == "R") {
        if(body.getLinearVelocity.x < 10f)
          body.applyForceToCenter(15f, 0f, true)
        if(!attacking)
          sprite.setRegion(walkRightAnimation.getKeyFrame(gameTime, true))
      }
      if(mov_h == "L") {
        if(body.getLinearVelocity.x > -10f)
          body.applyForceToCenter(-15f, 0f, true)
        if(!attacking)
          sprite.setRegion(walkLeftAnimation.getKeyFrame(gameTime, true))
      }
    }
  }

  def moveRight(gameTime:Float): Unit = {
    mov_h = "R"
    face_h = "R"
  }
  def moveLeft(gameTime:Float): Unit = {
    mov_h = "L"
    face_h = "L"
  }
  def moveUp(gameTime:Float): Unit = {
    mov_v = "U"
  }
  def moveDown(gameTime:Float): Unit = {
    mov_v = "D"
  }

  var jumping:Boolean = false
  var jumpMax:Float = 0.45f
  var lastJump:Float = 0
  def jump(gameTime:Float): Unit = {
    if(canJump()) {
      jumping = true
      lastJump = gameTime
    }
  }

  def canJump():Boolean = {

    for(contact:Contact <- GameLoader.world.getContactList()) {
      if(contact.getFixtureB.isSensor == false
        && contact.getFixtureA == fixtureBottom
        && contact.getFixtureB.getBody != body) {
        return true
      }
      if(contact.getFixtureA.isSensor == false
        && contact.getFixtureB == fixtureBottom
        && contact.getFixtureA.getBody != body) {
        return true
      }
    }
    return false
  }

  def canClimb():Boolean = {
    for(contact:Contact <- GameLoader.world.getContactList()) {
      if(contact.getFixtureB.isSensor == true
        && contact.getFixtureA == fixture
        && contact.getFixtureB.getBody != body
        && contact.getFixtureB.getUserData.isInstanceOf[Ladder]) {
        return true
      }
      if(contact.getFixtureA.isSensor == true
        && contact.getFixtureB == fixture
        && contact.getFixtureA.getBody != body
        && contact.getFixtureA.getUserData.isInstanceOf[Ladder]) {
        return true
      }
    }
    return false
  }

  var lastAttack:Float= 0
  var cooldown:Float = 0.3f
  var attacking:Boolean = false
  def attack(gameTime:Float): Unit = {
    if(lastAttack + cooldown > gameTime) {
      return
    }
    attacking = true
    lastAttack = gameTime
    var x = sprite.getX
    if(face_h.equalsIgnoreCase("R")) {
      x = sprite.getX + (sprite.getWidth)
    }
    var y = sprite.getY + (sprite.getHeight / 2)

    var b:Bullet = new Bullet(name+"_bullet_"+Math.random(),GameLoader.world, bulletSheet, x, y)
    b.mov_h = face_h
    b.attacker = this

  }

  var takingDamage:Boolean = false
  var lastDamage:Float = 0
  var damageCooldown:Float = 0.3f
  override def damage(source:Thing, amount:Integer): Unit = {
    def gameTime =  GameLoader.gameTime
    if(lastDamage + damageCooldown > gameTime) {
      return
    }
    takingDamage = true
    lastDamage = gameTime
    life -= amount
    if(life < 1) die(gameTime)
  }

  var dieing:Boolean = false
  var deathStart:Float = 0
  var deathEnd:Float = 0.3f
  def die(gameTime:Float): Unit = {
    if(dieing) {
      return
    }
    dieing = true
    deathStart = gameTime
  }

  override def draw(batch:Batch): Unit = {
    move(GameLoader.gameTime)
    super.draw(batch)
  }


  override def destroy() : Unit = {
    new Corpse(this.name + "_corpse", GameLoader.world, animationSheet.get(6), BodyDef.BodyType.StaticBody, GameUtil.metersToPixels(body.getPosition.x), GameUtil.metersToPixels(body.getPosition.y))
    light.setActive(false)
    GameLoader.world.destroyBody(body)
    GameLoader.thingDb -= this
    GameLoader.monsterDb.remove(name)
  }
}