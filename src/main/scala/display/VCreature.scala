package display

import box2dLight.{PointLight, PositionalLight, RayHandler}
import com.badlogic.gdx.graphics.{Color, Texture}
import com.badlogic.gdx.graphics.g2d.{Animation, Batch, Sprite, TextureRegion}
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d._
import game.{Attribute, Creature, Ladder, Thing}
import _root_.utils.Conversion

import scala.collection.JavaConversions._
import scala.collection.mutable.ListBuffer

protected object VCreature {
  object lilac {
    def sheet = new Texture("jayden.png")
    val sheetTextures:ListBuffer[TextureRegion] = ListBuffer()
    for(tr <- TextureRegion.split(sheet, 24, 24)) {
      for(tx <- tr) {
        sheetTextures.append(tx)
      }
    }
  }

  object snake {
    def sheet = new Texture("snake.png")
    val sheetTextures:ListBuffer[TextureRegion] = ListBuffer()
    for(tr <- TextureRegion.split(sheet, 24, 24)) {
      for(tx <- tr) {
        sheetTextures.append(tx)
      }
    }

  }

  object phoenix {
    def sheet = new Texture("phoenix.png")

    val sheetTextures: ListBuffer[TextureRegion] = ListBuffer()
    for (tr <- TextureRegion.split(sheet, 24, 24)) {
      for (tx <- tr) {
        sheetTextures.append(tx)
      }
    }
  }

  object spider {
    def sheet = new Texture("spider.png")

    val sheetTextures: ListBuffer[TextureRegion] = ListBuffer()
    for (tr <- TextureRegion.split(sheet, 24, 24)) {
      for (tx <- tr) {
        sheetTextures.append(tx)
      }
    }
  }

  object skeletonwarrior {
    def sheet = new Texture("skeleton_warrior.png")

    val sheetTextures: ListBuffer[TextureRegion] = ListBuffer()
    for (tr <- TextureRegion.split(sheet, 24, 24)) {
      for (tx <- tr) {
        sheetTextures.append(tx)
      }
    }
  }
}







protected class VCreature(creature:Creature, world:World, animationSheet:ListBuffer[TextureRegion]) extends VThing {

  var sprite:Sprite = new Sprite(animationSheet.head)
  sprite.setScale(creature.scaleX, creature.scaleY)

  var scaleX:Float = creature.scaleX
  var scaleY:Float = creature.scaleY
  var startX:Float = creature.startX
  var startY:Float = creature.startY

  var width:Float = sprite.getWidth * scaleX
  var height:Float = sprite.getHeight * scaleY

  var walkRightAnimation:Animation[TextureRegion]  = new Animation(0.15f, animationSheet(28), animationSheet(29))
  var standRightAnimation:Animation[TextureRegion]  = new Animation(0.15f, animationSheet(2))

  var walkLeftAnimation:Animation[TextureRegion] = new Animation(0.15f, animationSheet(24), animationSheet(25))
  var standLeftAnimation:Animation[TextureRegion]  = new Animation(0.15f, animationSheet(3))

  var climbAnimation:Animation[TextureRegion]  = new Animation(0.25f, animationSheet(8), animationSheet(9))

  var hurtAnimation:Animation[TextureRegion]  = new Animation(0.25f, animationSheet(48))
  var deathAnimation: Animation[TextureRegion] = new Animation(0.25f, animationSheet(72), animationSheet(73), animationSheet(74), animationSheet(75))

  var attackAnimationLeft: Animation[TextureRegion] = new Animation(0.25f, animationSheet(40), animationSheet(41))
  var attackAnimationRight: Animation[TextureRegion] = new Animation(0.25f, animationSheet(44), animationSheet(45))


  var body:Body = world
    .createBody(
      {
        val b: BodyDef = new BodyDef()
        b.`type` = BodyDef.BodyType.DynamicBody
        b.fixedRotation = true
        b.position.set(Conversion.pixelsToMeters(startX), Conversion.pixelsToMeters(startY))
        b
      })


  val fixture:Fixture = body.createFixture(
    {
      val f: FixtureDef = new FixtureDef()
      var shape: Shape = new CircleShape()

      f.filter.categoryBits = Thing.creature
      f.filter.maskBits = (Thing.floor | Thing.creature).toShort

      f.shape = shape
      shape.setRadius(Conversion.pixelsToMeters(height / 2.2f))
      f.friction = creature.get(Attribute.V_FRICTION).current
      f.density = creature.get(Attribute.V_DENSITY).current
      f.restitution = creature.get(Attribute.V_RESTITUTION).current
      f
    })
  fixture.setUserData(creature)

  var hitArea:Fixture = body.createFixture(
    {
      val f: FixtureDef = new FixtureDef()
      var shape: Shape = new CircleShape()
      f.shape = shape

      f.filter.categoryBits = Thing.creature
      f.filter.maskBits = (Thing.floor | Thing.creature).toShort

      shape.setRadius(Conversion.pixelsToMeters(height / 6.2f))

      f
    })
  hitArea.setUserData(creature)

  val fixtureBottom:Fixture = body.createFixture(
    {
      val f = new FixtureDef
      f.density = 0f
      f.isSensor = true
      f.shape = new PolygonShape()
      f.shape.asInstanceOf[PolygonShape]
        .setAsBox(Conversion.pixelsToMeters(width / 3f) / 2,
          Conversion.pixelsToMeters(height / 4.5f),
          new Vector2(0f, Conversion.pixelsToMeters(-1 * height / 2.5f)), 0)
      f
    })
  fixtureBottom.setUserData(creature)



  def slow(gameTime:Float):Unit = {
    body.setLinearVelocity(body.getLinearVelocity.x * 0.9f, body.getLinearVelocity.y * 0.9f)
  }
  def stop(gameTime: Float): Unit = {
    body.setGravityScale(1f)
    creature.movV = ""
    var mod:Float = 0.9f

    body.setLinearVelocity(body.getLinearVelocity.x * mod, body.getLinearVelocity.y)
    if (creature.faceH == "R") {
      sprite.setRegion(standRightAnimation.getKeyFrame(gameTime, true))
    } else {
      sprite.setRegion(standLeftAnimation.getKeyFrame(gameTime, true))
    }
  }


  def canJump: Boolean = {
    for (contact: Contact <- world.getContactList) {
      if (!contact.getFixtureB.isSensor
        && contact.getFixtureA == fixtureBottom
        && contact.getFixtureA != contact.getFixtureB
        && contact.getFixtureB.getBody != body) {
        if(contact.getFixtureB.getUserData.isInstanceOf[Thing]) {
          def t:Thing = contact.getFixtureB.getUserData.asInstanceOf[Thing]

          if(t.platform && creature.isOnTopOf(t)) {
            return true
          }
        }
      }
      if (!contact.getFixtureA.isSensor
        && contact.getFixtureB == fixtureBottom
        && contact.getFixtureA != contact.getFixtureB
        && contact.getFixtureA.getBody != body) {
        if(contact.getFixtureA.getUserData.isInstanceOf[Thing]) {
          def t:Thing = contact.getFixtureA.getUserData.asInstanceOf[Thing]
          if(t.platform  && creature.isOnTopOf(t)) {
            return true
          }

        }
      }
    }
    false
  }




  def canClimb: Boolean = {
    for (contact: Contact <- world.getContactList) {
      if (!contact.getFixtureB.isSensor
        && contact.getFixtureA == fixtureBottom
        && contact.getFixtureA != contact.getFixtureB
        && contact.getFixtureB.getBody != body) {
        if(contact.getFixtureB.getUserData.isInstanceOf[Thing]) {
          def t:Thing = contact.getFixtureB.getUserData.asInstanceOf[Thing]
          if(t.category == Thing.ladder && creature.isInside(t)) {
            return true
          }
        }
      }
      if (!contact.getFixtureA.isSensor
        && contact.getFixtureB == fixtureBottom
        && contact.getFixtureA != contact.getFixtureB
        && contact.getFixtureA.getBody != body) {
        if(contact.getFixtureA.getUserData.isInstanceOf[Thing]) {
          def t:Thing = contact.getFixtureA.getUserData.asInstanceOf[Thing]
          if(t.category == Thing.ladder  && creature.isInside(t)) {
            return true
          }

        }
      }
    }
    false
  }


  override def update(gameTime:Float):Unit = {
    this.fixture.setDensity(creature.get(Attribute.V_DENSITY).current)
    this.fixture.setFriction(creature.get(Attribute.V_FRICTION).current)
    this.fixture.setRestitution(creature.get(Attribute.V_RESTITUTION).current)
    this.body.setGravityScale(creature.get(Attribute.V_GRAVITY_SCALE).current)

    lastX = Conversion.metersToPixels(body.getPosition.x)
    lastY = Conversion.metersToPixels(body.getPosition.y)
    creature.lastX = lastX
    creature.lastY = lastY

    var isJumping = creature.isJumping
    creature.canJump = canJump
    creature.canClimb = canClimb

    this.body.setLinearVelocity(creature.get(Attribute.RUN_VELOCITY).current, creature.get(Attribute.JUMP_VELOCITY).current)

    if(creature.holding != null && creature.holding.attacking) {
      if(creature.faceH == "L") {
        sprite.setRegion(attackAnimationLeft.getKeyFrame(gameTime, true))
      } else if(creature.faceH == "R") {
        sprite.setRegion(attackAnimationRight.getKeyFrame(gameTime, true))
      }
    }  else if(creature.takingDamage) {
      sprite.setRegion(hurtAnimation.getKeyFrame(gameTime, true))
    }  else if (creature.movH == "R" && creature.movV != "") {
      sprite.setRegion(walkRightAnimation.getKeyFrame(gameTime, true))
    }  else if (creature.movH == "L"  && creature.movV != "") {
      sprite.setRegion(walkLeftAnimation.getKeyFrame(gameTime, true))
    }  else if (creature.movH == "R") {
      sprite.setRegion(walkRightAnimation.getKeyFrame(gameTime, true))
    } else if(creature.movH == "L") {
      sprite.setRegion(walkLeftAnimation.getKeyFrame(gameTime, true))
    } else if(creature.movV == "U") {
      sprite.setRegion(climbAnimation.getKeyFrame(gameTime, true))
    } else if(creature.movV == "D") {
      sprite.setRegion(climbAnimation.getKeyFrame(gameTime, true))
    } else if(creature.faceH == "L") {
      sprite.setRegion(standLeftAnimation.getKeyFrame(gameTime, true))
    } else if(creature.faceH == "R") {
      sprite.setRegion(standRightAnimation.getKeyFrame(gameTime, true))
    }
  }

}
