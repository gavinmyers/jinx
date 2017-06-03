package display
import com.badlogic.gdx.graphics.g2d.{Sprite, TextureRegion}
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d._
import game.{Thing, Tool}
import _root_.utils.Conversion

class VLadder(entity: Thing, world:World, sprites: scala.collection.mutable.Map[String,TextureRegion]) extends VThing {


  var sprite:Sprite = new Sprite(sprites("default"))
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
  body.setUserData(entity)
  body.setGravityScale(entity.get("gravityScale").current)

  val fixture: Fixture = body.createFixture({
    val f: FixtureDef = new FixtureDef()
    val shape: PolygonShape = new PolygonShape()
    f.isSensor = false
    f.filter.categoryBits = Thing.tool
    f.filter.maskBits = Thing.floor
    f.shape = shape
    shape.setAsBox(Conversion.pixelsToMeters((sprite.getHeight * scaleY) / 2), Conversion.pixelsToMeters((sprite.getWidth * scaleX) / 2))
    f.friction = entity.get("friction").current
    f.restitution = entity.get("restitution").current
    f
  })
  fixture.setUserData(entity)


  override def update(gameTime:Float):Unit = {
    if(entity.destroyed && sprites.contains("broken")) {
      this.sprite.setRegion(sprites("broken"))
    }
    lastX = Conversion.metersToPixels(body.getPosition.x)
    lastY = Conversion.metersToPixels(body.getPosition.y)
    entity.lastX = lastX
    entity.lastY = lastY
    entity.update(gameTime)
  }
}
