package display

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.{Animation, Sprite, TextureRegion}
import com.badlogic.gdx.physics.box2d._
import game.{Bullet, Thing, Tool}
import _root_.utils.Conversion

import scala.collection.mutable.ListBuffer

object VBullet {
  def sheet = new Texture("bullet1.png")
  val sheetTextures:ListBuffer[TextureRegion] = ListBuffer()
  for(tr <- TextureRegion.split(sheet, 24, 24)) {
    for(tx <- tr) {
      sheetTextures.append(tx)
    }
  }
}

class VBullet(entity: Bullet, world:World, animationSheet:ListBuffer[TextureRegion]) extends VThing {


  var attackAnimationRight:Animation = new Animation(0.15f, animationSheet(16),animationSheet(17),animationSheet(18),animationSheet(19),animationSheet(20),animationSheet(21),animationSheet(22),animationSheet(23))
  var attackAnimationLeft:Animation = new Animation(0.15f, animationSheet(16),animationSheet(17),animationSheet(18),animationSheet(19),animationSheet(20),animationSheet(21),animationSheet(22),animationSheet(23))

  var sprite:Sprite = new Sprite(animationSheet.head)
  sprite.setScale(entity.scaleX, entity.scaleY)

  var scaleX:Float = entity.scaleX
  var scaleY:Float = entity.scaleY
  var startX:Float = entity.startX
  var startY:Float = entity.startY

  var body:Body = world.createBody({
    val b: BodyDef = new BodyDef()
    b.`type` = BodyDef.BodyType.DynamicBody
    b.fixedRotation = true
    b.position.set(Conversion.pixelsToMeters(entity.startX), Conversion.pixelsToMeters(entity.startY))
    b
  })
  body.setUserData(sprite)

  val fixture: Fixture = body.createFixture({
    val f: FixtureDef = new FixtureDef()
    val shape: PolygonShape = new PolygonShape()
    f.isSensor = false
    f.filter.categoryBits = Thing.tool
    f.filter.maskBits = Thing.floor
    f.shape = shape
    shape.setAsBox(Conversion.pixelsToMeters(sprite.getHeight / 2), Conversion.pixelsToMeters(sprite.getWidth / 2))
    f.friction = 5f
    f
  })
  fixture.setUserData(entity)

  override def update(gameTime:Float):Unit = {
    entity.update(gameTime)
    if(entity.movH.equalsIgnoreCase("R")) {
      this.sprite.setRegion(attackAnimationRight.getKeyFrame(gameTime, true))
    } else {
      this.sprite.setRegion(attackAnimationLeft.getKeyFrame(gameTime, true))
    }
  }
}

