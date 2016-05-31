package display

import com.badlogic.gdx.graphics.g2d.{TextureRegion, Batch, Sprite}
import com.badlogic.gdx.physics.box2d._
import generics.{Creature, Thing}
import _root_.utils.Conversion

import scala.collection.mutable.ListBuffer

class VCreature(var creature:Creature, world:World, animationSheet:ListBuffer[TextureRegion]) extends VThing {

  var sprite:Sprite = new Sprite(animationSheet.head)
  sprite.setScale(creature.scaleX, creature.scaleY)

  var scaleX:Float = creature.scaleX
  var scaleY:Float = creature.scaleY
  var posX:Float = creature.posX
  var posY:Float = creature.posY

  var body:Body = world.createBody({
    val b: BodyDef = new BodyDef()
    b.`type` = BodyDef.BodyType.StaticBody
    b.fixedRotation = true
    b.position.set(Conversion.pixelsToMeters(creature.posX), Conversion.pixelsToMeters(creature.posY))
    b
  })
  body.setUserData(sprite)

  val fixture: Fixture = body.createFixture({
    val f: FixtureDef = new FixtureDef()
    val shape: PolygonShape = new PolygonShape()
    f.isSensor = false
    f.shape = shape
    shape.setAsBox(Conversion.pixelsToMeters(sprite.getHeight / 2), Conversion.pixelsToMeters(sprite.getWidth / 2))
    f.friction = 5f
    f
  })
  fixture.setUserData(this)
}
