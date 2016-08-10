package display

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.{Animation, Sprite, TextureRegion}
import com.badlogic.gdx.physics.box2d._
import game.{Bullet, Thing, Tool}
import _root_.utils.Conversion

import scala.collection.mutable.ListBuffer

protected object VBullet {
  def sheet = new Texture("bullet1.png")
  val sheetTextures:ListBuffer[TextureRegion] = ListBuffer()
  for(tr <- TextureRegion.split(sheet, 24, 24)) {
    for(tx <- tr) {
      sheetTextures.append(tx)
    }
  }

  object explosion {
    var attackAnimationRight:Animation = new Animation(0.15f, sheetTextures(16),sheetTextures(17),sheetTextures(18),sheetTextures(19),sheetTextures(20),sheetTextures(21),sheetTextures(22),sheetTextures(23))
    var attackAnimationLeft:Animation = new Animation(0.15f, sheetTextures(16),sheetTextures(17),sheetTextures(18),sheetTextures(19),sheetTextures(20),sheetTextures(21),sheetTextures(22),sheetTextures(23))
  }

  object fire {
    var attackAnimationRight:Animation = new Animation(0.15f, sheetTextures(32),sheetTextures(33),sheetTextures(34),sheetTextures(35))
    var attackAnimationLeft:Animation = new Animation(0.15f, sheetTextures(32),sheetTextures(33),sheetTextures(34),sheetTextures(35))
  }

  object slash {
    var attackAnimationRight:Animation = new Animation(0.15f, sheetTextures(0),sheetTextures(1),sheetTextures(2))
    var attackAnimationLeft:Animation = new Animation(0.15f, sheetTextures(8),sheetTextures(9),sheetTextures(10))
  }

  object catchem {
    var attackAnimationRight:Animation = new Animation(0.15f, sheetTextures(40),sheetTextures(41),sheetTextures(42),sheetTextures(43))
    var attackAnimationLeft:Animation = new Animation(0.15f, sheetTextures(40),sheetTextures(41),sheetTextures(42),sheetTextures(43))
  }
}


protected class VBullet(entity: Bullet, world:World, animationSheet:ListBuffer[TextureRegion]) extends VThing {


  var attackAnimationRight:Animation = VBullet.explosion.attackAnimationRight
  var attackAnimationLeft:Animation = VBullet.explosion.attackAnimationLeft

  if(entity.effect == Bullet.catchem) {
    attackAnimationRight = VBullet.catchem.attackAnimationRight
    attackAnimationLeft = VBullet.catchem.attackAnimationLeft
  }

  if(entity.effect == Bullet.fire) {
    attackAnimationRight = VBullet.fire.attackAnimationRight
    attackAnimationLeft = VBullet.fire.attackAnimationLeft
  }

  if(entity.effect == Bullet.slash) {
    attackAnimationRight = VBullet.slash.attackAnimationRight
    attackAnimationLeft = VBullet.slash.attackAnimationLeft
  }

  var sprite:Sprite = new Sprite(animationSheet.head)
  sprite.setScale(entity.scaleX, entity.scaleY)

  var scaleX:Float = entity.scaleX
  var scaleY:Float = entity.scaleY
  var startX:Float = entity.startX
  var startY:Float = entity.startY
  var forceX:Float = entity.forceX
  var forceY:Float = entity.forceY
  var force:Boolean = false

  var body:Body = world.createBody({
    val b: BodyDef = new BodyDef()
    b.`type` = BodyDef.BodyType.DynamicBody
    b.fixedRotation = true
    b.position.set(Conversion.pixelsToMeters(entity.startX), Conversion.pixelsToMeters(entity.startY))
    b
  })
  body.setUserData(sprite)
  body.setGravityScale(entity.weight)

  val fixture: Fixture = body.createFixture({
    val f: FixtureDef = new FixtureDef()
    val shape: PolygonShape = new PolygonShape()
    f.isSensor = false
    f.filter.categoryBits = Thing.bullet
    f.filter.maskBits = Thing.floor
    f.shape = shape
    shape.setAsBox(Conversion.pixelsToMeters(sprite.getHeight / 2), Conversion.pixelsToMeters(sprite.getWidth / 2))
    f.friction = 5f
    f
  })
  fixture.setUserData(entity)

  override def update(gameTime:Float):Unit = {
    lastX = Conversion.metersToPixels(body.getPosition.x)
    lastY = Conversion.metersToPixels(body.getPosition.y)
    entity.lastX = lastX
    entity.lastY = lastY
    entity.update(gameTime)
    if(entity.movH.equalsIgnoreCase("R")) {
      this.sprite.setRegion(attackAnimationRight.getKeyFrame(gameTime, true))
    } else {
      this.sprite.setRegion(attackAnimationLeft.getKeyFrame(gameTime, true))
    }
    if((forceX > 0 || forceY > 0) && force == false) {
      force = true
      body.applyForceToCenter(forceX, forceY, true)
    }
  }
}

