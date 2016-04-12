import box2dLight.{PointLight, PositionalLight}
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.{Batch, Animation, Sprite, TextureRegion}
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d._
import scala.collection.JavaConversions._
import scala.collection.mutable.ListBuffer

class Being(name:String,
            world:World,
            animationSheet:ListBuffer[TextureRegion],
            posX:Float,
            posY:Float,
            val scaleX:Float,
            val scaleY:Float)
  extends Thing() {

  this.created = GameLoader.gameTime
  var light:PositionalLight = _
  var fixture: Fixture = _
  var fixtureBottom: Fixture = _
  var hitArea: Fixture = _
  var weapon:Weapon = _
  var brain:Brain = _
  var canFly:Boolean = false
  var life:Float = 100
  var width:Float = 0
  var height:Float = 0

  var fireResistance:Float = 0f
  var iceResistance:Float = 0f
  var poisonResistance:Float = 0f
  var stoneResistance:Float = 0f
  var acidResistance:Float = 0f


  override def init(): Unit = {
    this.sprite = new Sprite(animationSheet.head)
    sprite.setScale(scaleX, scaleY)
    this.width = sprite.getWidth * scaleX
    this.height = sprite.getHeight * scaleY
    this.weapon = new Weapon(this)
    this.brain = new Brain(this)

    this.body = world
      .createBody(
        {val b: BodyDef = new BodyDef()
          b.`type` = BodyDef.BodyType.DynamicBody
          b.fixedRotation = true
          b.position.set(GameUtil.pixelsToMeters(posX), GameUtil.pixelsToMeters(posY))
          b})



    this.fixture = body.createFixture(
      {val f:FixtureDef = new FixtureDef()
        var shape:Shape = new CircleShape()
        f.filter.categoryBits = 0x2
        f.filter.maskBits = 0x1
        f.shape = shape
        shape.setRadius(GameUtil.pixelsToMeters(height / 2.2f))
        f.friction = 0f; f})


    this.hitArea = body.createFixture(
      {val f:FixtureDef = new FixtureDef()
        var shape:Shape = new CircleShape()
        f.shape = shape
        shape.setRadius(GameUtil.pixelsToMeters(height / 6.2f))
        f.friction = 0f; f})

    this.fixtureBottom = body.createFixture(
      {val f = new FixtureDef
        f.density = 0f
        f.isSensor = true
        f.shape = new PolygonShape()
        f.shape.asInstanceOf[PolygonShape]
          .setAsBox(GameUtil.pixelsToMeters(width / 3f) / 2,
            GameUtil.pixelsToMeters(height / 4.5f),
            new Vector2(0f, GameUtil.pixelsToMeters(-1 * height / 2.5f)), 0)
        f})

    var fixtureTop = body.createFixture(
      {val f = new FixtureDef
        f.isSensor = true
        f.density = 0f
        f.shape = new PolygonShape()
        f.shape.asInstanceOf[PolygonShape]
          .setAsBox(GameUtil.pixelsToMeters(width / 3f),
            GameUtil.pixelsToMeters(height / 4.5f),
            new Vector2(0f, GameUtil.pixelsToMeters(height / 2.5f)), 0)
        f})
  }


  var walkRightAnimation:Animation = new Animation(0.15f, animationSheet(28),animationSheet(29))
  var standRightAnimation:Animation = new Animation(0.15f, animationSheet(2))

  var walkLeftAnimation:Animation = new Animation(0.15f, animationSheet(24),animationSheet(25))
  var standLeftAnimation:Animation = new Animation(0.15f, animationSheet(3))

  var climbAnimation:Animation = new Animation(0.25f, animationSheet(8),animationSheet(9))

  var hurtAnimation:Animation = new Animation(0.25f, animationSheet(48))
  var deathAnimation:Animation = new Animation(0.25f, animationSheet(72),animationSheet(73),animationSheet(74),animationSheet(75))

  var attackAnimationLeft:Animation = new Animation(0.25f, animationSheet(40),animationSheet(41))
  var attackAnimationRight:Animation = new Animation(0.25f, animationSheet(44),animationSheet(45))



  fixture.setUserData(this)

  GameLoader.monsterDb += name -> this
  GameLoader.thingDb += this

  def fall(gameTime:Float): Unit = {
    jumping = false
    mov_v = ""
    body.setLinearVelocity(body.getLinearVelocity.x, body.getLinearVelocity.y * 0.9f)
  }

  def stop(gameTime:Float): Unit = {
    body.setGravityScale(1f)
    mov_h = ""
    body.setLinearVelocity(body.getLinearVelocity.x * 0.9f, body.getLinearVelocity.y)
    if(face_h == "R" && !weapon.attacking) {
      sprite.setRegion(standRightAnimation.getKeyFrame(gameTime, true))
    } else if (face_h == "L"  && !weapon.attacking) {
      sprite.setRegion(standLeftAnimation.getKeyFrame(gameTime, true))
    }
  }
  override def update(gameTime:Float): Unit = {
    if(brain != null) {
      brain.update(gameTime)
    }

    if(weapon != null) {
      weapon.update(gameTime)
    }

    if(dieing && deathStart + deathEnd < gameTime) {
      destroy()
      return
    }
    if(lastDamage + damageCooldown < gameTime) {
      takingDamage = false
    }

  }

  var jumpMaxVelocity = 15f
  var runMaxVelocity = 2.5f
  override def move(gameTime:Float): Unit = {
    if(canClimb || canFly) {
      body.setGravityScale(0f)
    } else {
      body.setGravityScale(1f)
    }

    if(dieing) {
      sprite.setRegion(deathAnimation.getKeyFrame(gameTime, true))

    } else if(takingDamage) {
      sprite.setRegion(hurtAnimation.getKeyFrame(gameTime, true))

    } else if(weapon.attacking) {
      if(face_h == "R") {
        sprite.setRegion(attackAnimationRight.getKeyFrame(gameTime, true))
      } else if(face_h == "L") {
        sprite.setRegion(attackAnimationLeft.getKeyFrame(gameTime, true))
      }

    } else if(!canClimb && mov_h == "" && !jumping) {
      stop(gameTime)

    } else {

      if(jumping) {
        if(body.getLinearVelocity.y < jumpMaxVelocity && lastJump + jumpMax > gameTime) {
          body.applyForceToCenter(0f, 250f, true)
        } else {
          fall(gameTime)
        }
      } else if(canClimb) {
        if(mov_v == "U") {
          body.setLinearVelocity(body.getLinearVelocity.x * .9f, 4.5f)
          sprite.setRegion(climbAnimation.getKeyFrame(gameTime, true))
        } else if(mov_v == "D") {
          body.setLinearVelocity(body.getLinearVelocity.x * .9f, -4.5f)
          sprite.setRegion(climbAnimation.getKeyFrame(gameTime, true))
        } else {
          body.setLinearVelocity(body.getLinearVelocity.x * .95f, body.getLinearVelocity.y * 0.7f)
        }

      }

      if(mov_h == "R") {
        if(body.getLinearVelocity.x < runMaxVelocity)
          body.applyForceToCenter(15f, 0f, true)
        if(!weapon.attacking)
          sprite.setRegion(walkRightAnimation.getKeyFrame(gameTime, true))
      }
      if(mov_h == "L") {
        if(body.getLinearVelocity.x > runMaxVelocity * -1)
          body.applyForceToCenter(-15f, 0f, true)
        if(!weapon.attacking)
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

    for(contact:Contact <- GameLoader.world.getContactList) {
      if(!contact.getFixtureB.isSensor
        && contact.getFixtureA == fixtureBottom
        && contact.getFixtureB.getBody != body) {
        return true
      }
      if(!contact.getFixtureA.isSensor
        && contact.getFixtureB == fixtureBottom
        && contact.getFixtureA.getBody != body) {
        return true
      }
    }
    false
  }

  def canClimb:Boolean = {
    for(contact:Contact <- GameLoader.world.getContactList) {
      if(contact.getFixtureB.isSensor
        && contact.getFixtureA == hitArea
        && contact.getFixtureB.getBody != body
        && contact.getFixtureB.getUserData.isInstanceOf[Ladder]) {
        return true
      }
      if(contact.getFixtureA.isSensor
        && contact.getFixtureB == hitArea
        && contact.getFixtureA.getBody != body
        && contact.getFixtureA.getUserData.isInstanceOf[Ladder]) {
        return true
      }
    }
    false
  }

  def attack(gameTime:Float): Unit = {
    weapon.attack(gameTime)
  }

  var takingDamage:Boolean = false
  var lastDamage:Float = 0
  var damageCooldown:Float = 0.3f
  override def damage(source:Thing, amount:Float): Unit = {

    if(!source.isInstanceOf[Bullet]) {
      return //eh?
    }

    def b:Bullet = source.asInstanceOf[Bullet]

    def gameTime =  GameLoader.gameTime
    if(lastDamage + damageCooldown > gameTime) {
      return
    }

    if(b.ice) {
      //this.sprite.setColor(this.sprite.getColor.r / 5f,this.sprite.getColor.g / 5f,this.sprite.getColor.b * 2f, 1f)
    }
    if(b.fire) {
      //this.sprite.setColor(this.sprite.getColor.r * 2f,this.sprite.getColor.g / 5f,this.sprite.getColor.b / 5, 1f)
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
    GameLoader.font.getData.setScale(0.5f)
    super.draw(batch)
    GameLoader.font.draw(GameLoader.batch, this.life.toInt.toString, GameUtil.metersToPixels(this.body.getPosition.x), GameUtil.metersToPixels(this.body.getPosition.y + GameUtil.pixelsToMeters(sprite.getHeight * scaleY)))
  }


  override def destroy() : Unit = {
    new Corpse(this.name + "_corpse", GameLoader.world, animationSheet(6), BodyDef.BodyType.StaticBody, GameUtil.metersToPixels(body.getPosition.x), GameUtil.metersToPixels(body.getPosition.y), scaleX, scaleY)
    if(light != null) {
      light.setActive(false)
    }
    GameLoader.monsterDb.remove(name)
    super.destroy()
  }
}