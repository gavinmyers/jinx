package display

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.{Sprite, Animation, TextureRegion}
import com.badlogic.gdx.math.Vector2
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

protected class VNotification(entity: Notification, world:World) extends VThing {


  var sprite:Sprite = new Sprite(entity.message match {
    case "?" => VNotification.sheetTextures(37)
    case "L" => VNotification.sheetTextures(32)
    case "N" => VNotification.sheetTextures(38)
    case "Y" => VNotification.sheetTextures(39)
    case _ => VNotification.sheetTextures(38)
  })

  sprite.setScale(entity.scaleX, entity.scaleY)

  var scaleX: Float = entity.scaleX
  var scaleY: Float = entity.scaleY
  var startY: Float = entity.startY
  var startX: Float = entity.startX



  var body:Body = world.createBody({

    val b: BodyDef = new BodyDef()
    b.`type` = BodyDef.BodyType.StaticBody
    b.fixedRotation = true
    b.position.set(Conversion.pixelsToMeters(startX), Conversion.pixelsToMeters(startY))
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
