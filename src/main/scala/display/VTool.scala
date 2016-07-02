package display

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.{TextureRegion, Sprite}
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d._
import game.{Tool, Thing}
import _root_.utils.Conversion

import scala.collection.mutable.ListBuffer

object VTool {
  def sheet = new Texture("things.png")
  val sheetTextures:ListBuffer[TextureRegion] = ListBuffer()
  for(tr <- TextureRegion.split(sheet, 24, 24)) {
    for(tx <- tr) {
      sheetTextures.append(tx)
    }
  }

  def lantern =  VTool.sheetTextures(13)
  def ironsword = VTool.sheetTextures(25)
  def cupcake = VTool.sheetTextures(24)
  def pigmask = VTool.sheetTextures(18)
  def medicinewheel = VTool.sheetTextures(16)
  def chest = VTool.sheetTextures(26)

}

class VTool(entity: Tool, world:World, textureRegion: TextureRegion) extends VThing {

  var sprite:Sprite = new Sprite(textureRegion)
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
    f.filter.maskBits = Thing.floor
    f.shape = shape
    shape.setAsBox(Conversion.pixelsToMeters(sprite.getHeight / 2), Conversion.pixelsToMeters(sprite.getWidth / 2))
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
        .setAsBox(Conversion.pixelsToMeters(sprite.getWidth / 3f) / 2,
          Conversion.pixelsToMeters(sprite.getHeight / 4.5f),
          new Vector2(0f, Conversion.pixelsToMeters(-1 * sprite.getHeight / 2.5f)), 0)
      f
    })
  fixtureBottom.setUserData(entity)

  override def update(gameTime:Float):Unit = {
    if(entity.destroyed) {
      this.sprite.setAlpha(0.25f)
    }
    entity.update(gameTime)
  }
}
