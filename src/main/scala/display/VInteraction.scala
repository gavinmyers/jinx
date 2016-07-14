package display

import com.badlogic.gdx.graphics.g2d.{Sprite, TextureRegion}
import com.badlogic.gdx.physics.box2d._
import game.{Thing, Tool}
import _root_.utils.Conversion

protected class VInteraction(entity: Thing, world:World, textureRegion: TextureRegion) extends VThing {

  var sprite:Sprite = new Sprite(textureRegion)
  sprite.setScale(entity.scaleX, entity.scaleY)

  var scaleX:Float = entity.scaleX
  var scaleY:Float = entity.scaleY
  var startX:Float = entity.startX
  var startY:Float = entity.startY

  var body:Body = world.createBody({
    val b: BodyDef = new BodyDef()
    b.`type` = BodyDef.BodyType.StaticBody
    b.fixedRotation = true
    b.position.set(Conversion.pixelsToMeters(entity.startX), Conversion.pixelsToMeters(entity.startY))
    b
  })
  body.setUserData(sprite)

  val fixture: Fixture = body.createFixture({
    val f: FixtureDef = new FixtureDef()
    val shape: PolygonShape = new PolygonShape()
    f.isSensor = false
    f.filter.categoryBits = Thing.interaction
    f.filter.maskBits = Thing.floor
    f.shape = shape
    shape.setAsBox(Conversion.pixelsToMeters(entity.width / 2), Conversion.pixelsToMeters(entity.height / 2))
    f.friction = 5f
    f
  })
  fixture.setUserData(entity)
}

