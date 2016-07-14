package display

import com.badlogic.gdx.graphics.g2d.{Sprite, TextureRegion}
import com.badlogic.gdx.physics.box2d._
import game.{Thing, Tool}
import _root_.utils.Conversion


protected class VStatus(var startX:Float, var startY:Float,  world:World, textureRegion: TextureRegion) extends VThing {

  var sprite:Sprite = new Sprite(textureRegion)

  var scaleX:Float = 1.0f
  var scaleY:Float = 1.0f
  sprite.setScale(scaleX, scaleY)


  var body:Body = world.createBody({
    val b: BodyDef = new BodyDef()
    b.`type` = BodyDef.BodyType.StaticBody
    b.fixedRotation = true
    b.position.set(Conversion.pixelsToMeters(startX), Conversion.pixelsToMeters(startY))
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

  override def update(gameTime:Float):Unit = {
  }
}
