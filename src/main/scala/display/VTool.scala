package display

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.{TextureRegion, Sprite}
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d._
import game.{Tool, Thing}
import _root_.utils.Conversion

import scala.collection.mutable.ListBuffer

protected object VTool {
  def sheet = new Texture("things.png")
  val sheetTextures:ListBuffer[TextureRegion] = ListBuffer()
  for(tr <- TextureRegion.split(sheet, 24, 24)) {
    for(tx <- tr) {
      sheetTextures.append(tx)
    }
  }

  var tools:scala.collection.mutable.Map[String,scala.collection.mutable.Map[String,TextureRegion]] = scala.collection.mutable.Map[String, scala.collection.mutable.Map[String,TextureRegion]]()
  tools("lantern") = scala.collection.mutable.Map[String,TextureRegion]()
  tools("lantern")("default") = VTool.sheetTextures(100)

  tools("ironsword") = scala.collection.mutable.Map[String,TextureRegion]()
  tools("ironsword")("default") = VTool.sheetTextures(24)

  tools("chest") = scala.collection.mutable.Map[String,TextureRegion]()
  tools("chest")("default") = VTool.sheetTextures(104)
  tools("chest")("broken") = VTool.sheetTextures(105)


  tools("key") = scala.collection.mutable.Map[String,TextureRegion]()
  tools("key")("default") = VTool.sheetTextures(128)

  tools("corpse") = scala.collection.mutable.Map[String,TextureRegion]()
  tools("corpse")("default") = VTool.sheetTextures(132)

  tools("catchem") = scala.collection.mutable.Map[String,TextureRegion]()
  tools("catchem")("default") = VTool.sheetTextures(140)

  tools("woodblock") = scala.collection.mutable.Map[String,TextureRegion]()
  tools("woodblock")("default") = VTool.sheetTextures(144)
  tools("woodblock")("broken") = VTool.sheetTextures(145)

}

protected class VTool(entity: Tool, world:World, sprites: scala.collection.mutable.Map[String,TextureRegion]) extends VThing {

  var sprite:Sprite = new Sprite(sprites("default"))
  sprite.setScale(entity.scaleX, entity.scaleY)

  var scaleX:Float = entity.scaleX
  var scaleY:Float = entity.scaleY
  var startX:Float = entity.startX
  var startY:Float = entity.startY

  var body:Body = world.createBody({
    val b: BodyDef = new BodyDef()
    b.`type` = BodyDef.BodyType.DynamicBody
    b.fixedRotation = false
    b.position.set(Conversion.pixelsToMeters(entity.startX), Conversion.pixelsToMeters(entity.startY))
    b
  })
  body.setUserData(sprite)
  body.setGravityScale(entity.weight)

  val fixture: Fixture = body.createFixture({
    val f: FixtureDef = new FixtureDef()
    val shape: PolygonShape = new PolygonShape()
    f.isSensor = false
    f.filter.categoryBits = Thing.tool
    if(!entity.wall)
      f.filter.maskBits = Thing.floor
    f.shape = shape
    shape.setAsBox(Conversion.pixelsToMeters((sprite.getHeight * scaleY) / 2), Conversion.pixelsToMeters((sprite.getWidth * scaleX) / 2))
    f.friction = 5f
    f
  })
  fixture.setUserData(entity)

  val fixtureBottom:Fixture = body.createFixture(
    {
      val f = new FixtureDef
      f.density = 0f
      f.isSensor = true
      f.shape = new PolygonShape()
      f.shape.asInstanceOf[PolygonShape]
        .setAsBox(Conversion.pixelsToMeters((sprite.getWidth * scaleX) / 3f) / 2,
          Conversion.pixelsToMeters((sprite.getHeight * scaleY) / 4.5f),
          new Vector2(0f, Conversion.pixelsToMeters(-1 * (sprite.getHeight * scaleY) / 2.5f)), 0)
      f
    })
  fixtureBottom.setUserData(entity)

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
