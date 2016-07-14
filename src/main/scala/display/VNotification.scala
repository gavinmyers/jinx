package display

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.{Sprite, Animation, TextureRegion}
import com.badlogic.gdx.physics.box2d._
import game.{Thing, Notification, Bullet}
import _root_.utils.Conversion

import scala.collection.mutable.ListBuffer


protected object VNotification {
  def sheet = new Texture("effects.png")
  val sheetTextures:ListBuffer[TextureRegion] = ListBuffer()
  for(tr <- TextureRegion.split(sheet, 12, 12)) {
    for(tx <- tr) {
      sheetTextures.append(tx)
    }
  }
}

protected class VNotification(entity: Notification, world:World) {
  var sprite:Sprite = new Sprite(VNotification.sheetTextures.head)


  var body:Body = world.createBody({
    val b: BodyDef = new BodyDef()
    b.`type` = BodyDef.BodyType.StaticBody
    b.fixedRotation = true
    b.position.set(Conversion.pixelsToMeters(entity.startX), Conversion.pixelsToMeters(entity.startY))
    b
  })
  body.setUserData(sprite)
  body.setGravityScale(entity.weight)

  val fixture: Fixture = body.createFixture({
    val f: FixtureDef = new FixtureDef()
    val shape: PolygonShape = new PolygonShape()
    f.isSensor = true
    f.filter.categoryBits = Thing.bullet
    f.filter.maskBits = Thing.floor
    f.shape = shape
    shape.setAsBox(Conversion.pixelsToMeters(sprite.getHeight / 2), Conversion.pixelsToMeters(sprite.getWidth / 2))
    f.friction = 5f
    f
  })
  fixture.setUserData(entity)
}
