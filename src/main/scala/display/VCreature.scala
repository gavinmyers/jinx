package display

import box2dLight.{RayHandler, PositionalLight, PointLight}
import com.badlogic.gdx.graphics.{Texture, Color}
import com.badlogic.gdx.graphics.g2d.{Animation, TextureRegion, Batch, Sprite}
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d._
import game.{Ladder, Creature, Thing}
import _root_.utils.Conversion
import scala.collection.JavaConversions._

import scala.collection.mutable.ListBuffer

object VLilac {
  def sheet = new Texture("jayden.png")
  val sheetTextures:ListBuffer[TextureRegion] = ListBuffer()
  for(tr <- TextureRegion.split(sheet, 24, 24)) {
    for(tx <- tr) {
      sheetTextures.append(tx)
    }
  }
}

object VSnake {
  def sheet = new Texture("snake.png")
  val sheetTextures:ListBuffer[TextureRegion] = ListBuffer()
  for(tr <- TextureRegion.split(sheet, 24, 24)) {
    for(tx <- tr) {
      sheetTextures.append(tx)
    }
  }

}

object VPhoenix {
  def sheet = new Texture("phoenix.png")

  val sheetTextures: ListBuffer[TextureRegion] = ListBuffer()
  for (tr <- TextureRegion.split(sheet, 24, 24)) {
    for (tx <- tr) {
      sheetTextures.append(tx)
    }
  }
}

class VCreature(creature:Creature, world:World, animationSheet:ListBuffer[TextureRegion]) extends VThing {

  var sprite:Sprite = new Sprite(animationSheet.head)
  sprite.setScale(creature.scaleX, creature.scaleY)

  var scaleX:Float = creature.scaleX
  var scaleY:Float = creature.scaleY
  var startX:Float = creature.startX
  var startY:Float = creature.startY

  var width:Float = sprite.getWidth * scaleX
  var height:Float = sprite.getHeight * scaleY

  var walkRightAnimation: Animation = new Animation(0.15f, animationSheet(28), animationSheet(29))
  var standRightAnimation: Animation = new Animation(0.15f, animationSheet(2))

  var walkLeftAnimation: Animation = new Animation(0.15f, animationSheet(24), animationSheet(25))
  var standLeftAnimation: Animation = new Animation(0.15f, animationSheet(3))

  var climbAnimation: Animation = new Animation(0.25f, animationSheet(8), animationSheet(9))

  var hurtAnimation: Animation = new Animation(0.25f, animationSheet(48))
  var deathAnimation: Animation = new Animation(0.25f, animationSheet(72), animationSheet(73), animationSheet(74), animationSheet(75))

  var attackAnimationLeft: Animation = new Animation(0.25f, animationSheet(40), animationSheet(41))
  var attackAnimationRight: Animation = new Animation(0.25f, animationSheet(44), animationSheet(45))


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
      f.friction = 0f
      f.density = 3f
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
      f.friction = 0f
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

  var fixtureTop = body.createFixture(
    {
      val f = new FixtureDef
      f.isSensor = true
      f.density = 0f
      f.shape = new PolygonShape()
      f.shape.asInstanceOf[PolygonShape]
        .setAsBox(Conversion.pixelsToMeters(width / 3f),
          Conversion.pixelsToMeters(height / 4.5f),
          new Vector2(0f, Conversion.pixelsToMeters(height / 2.5f)), 0)
      f
    })
  fixtureTop.setUserData(creature)

  def fall(gameTime: Float): Unit = {
    creature.jumping = false
    creature.movV = ""

    body.setLinearVelocity(body.getLinearVelocity.x, body.getLinearVelocity.y * 0.9f)
  }

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

  def canJump(): Boolean = {
    for (contact: Contact <- world.getContactList) {
      if (!contact.getFixtureB.isSensor
        && contact.getFixtureA == fixtureBottom
        && contact.getFixtureB.getBody != body) {
        return true
      }
      if (!contact.getFixtureA.isSensor
        && contact.getFixtureB == fixtureBottom
        && contact.getFixtureA.getBody != body) {
        return true
      }
    }
    false
  }

  def canClimb(): Boolean = {

    for (contact: Contact <- world.getContactList) {
      if (contact.getFixtureA.getUserData == creature && contact.getFixtureB.getUserData.isInstanceOf[Ladder]) {
        return true
      }
      if (contact.getFixtureB.getUserData == creature && contact.getFixtureA.getUserData.isInstanceOf[Ladder]) {
        return true
      }
    }
    false

  }

  override def update(gameTime:Float):Unit = {
    lastX = Conversion.metersToPixels(body.getPosition.x)
    lastY = Conversion.metersToPixels(body.getPosition.y)
    creature.lastX = lastX
    creature.lastY = lastY

    if(creature.holding != null) {
      creature.holding.update(gameTime)
    }

    if(canJump && (body.getLinearVelocity.x > creature.get("run_max_velocity") || body.getLinearVelocity.x < creature.get("run_max_velocity") * -1)) {
      slow(gameTime)
    }

    body.setGravityScale(creature.weight)

    if(creature.jump) {
      if (canJump()) {
        creature.jumping = true
        creature.lastJump = gameTime
      }
      creature.jump = false
    }

    if (creature.dieing) {
      sprite.setRegion(deathAnimation.getKeyFrame(gameTime, true))

    } else if (creature.takingDamage) {
      sprite.setRegion(hurtAnimation.getKeyFrame(gameTime, true))
     } else if (creature.holding != null && creature.holding.attacking) {
        if (creature.faceH == "R") {
          sprite.setRegion(attackAnimationRight.getKeyFrame(gameTime, true))
        } else if (creature.faceH == "L") {
          sprite.setRegion(attackAnimationLeft.getKeyFrame(gameTime, true))
        }
    } else if (!canClimb && creature.movH == "" && !creature.jumping) {
      stop(gameTime)
    } else {

      if (creature.jumping) {
        if (body.getLinearVelocity.y < creature.get("jump_max_velocity") && creature.lastJump + creature.get("jump_max") > gameTime) {
          var h:Float = 0f
          if("R".equalsIgnoreCase(creature.movH)) {
            h = 50f
          } else if("L".equalsIgnoreCase(creature.movH)) {
            h = -50f
          }
          body.applyForceToCenter(h, 250f, true)
        } else {
          fall(gameTime)
        }
      } else if (canClimb) {
        if (creature.movV == "U") {
          body.setLinearVelocity(body.getLinearVelocity.x * .9f, 4.5f)
          sprite.setRegion(climbAnimation.getKeyFrame(gameTime, true))
        } else if (creature.movV == "D") {
          body.setLinearVelocity(body.getLinearVelocity.x * .9f, -4.5f)
          sprite.setRegion(climbAnimation.getKeyFrame(gameTime, true))
        } else {
          body.setLinearVelocity(body.getLinearVelocity.x * .95f, body.getLinearVelocity.y * 0.7f)
        }

      }

      if (creature.movH == "R") {
        if (body.getLinearVelocity.x < creature.get("run_max_velocity"))
          body.applyForceToCenter(15f, 0f, true)
        //if (!weapon.attacking)
        sprite.setRegion(walkRightAnimation.getKeyFrame(gameTime, true))
      }
      if (creature.movH == "L") {
        if (body.getLinearVelocity.x > creature.get("run_max_velocity") * -1)
          body.applyForceToCenter(-15f, 0f, true)
        //if (!weapon.attacking)
        sprite.setRegion(walkLeftAnimation.getKeyFrame(gameTime, true))
      }
    }
  }

}
