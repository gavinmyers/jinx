import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.{Sprite, TextureRegion}
import com.badlogic.gdx.physics.box2d._

import scala.collection.mutable.ListBuffer

object Tool {
  def sheet = new Texture("things.png")
  val sheetTextures:ListBuffer[TextureRegion] = ListBuffer()
  for(tr <- TextureRegion.split(sheet, 24, 24)) {
    for(tx <- tr) {
      sheetTextures.append(tx)
    }
  }
}


class Tool(world:World, texture:TextureRegion, posX:Float, posY:Float) extends Thing() {
  var fixture: Fixture = _

  override def init(): Unit = {
    this.sprite = new Sprite(texture)

    var width = sprite.getWidth
    var height = sprite.getHeight

    this.body = GameLoader.world
      .createBody(
        {val b: BodyDef = new BodyDef()
          val x = posX
          val y = posY
          b.`type` = BodyDef.BodyType.DynamicBody
          b.fixedRotation = true
          b.position.set(GameUtil.pixelsToMeters(x), GameUtil.pixelsToMeters(y))
          b})

    this.fixture = body.createFixture(
      {val f:FixtureDef = new FixtureDef()
        var shape:PolygonShape = new PolygonShape()
        f.isSensor = false
        f.filter.categoryBits = Thing.tool
        f.filter.maskBits = Thing.floor
        f.shape = shape
        shape.setAsBox(GameUtil.pixelsToMeters(width / 2f), GameUtil.pixelsToMeters(height / 2f))
        f.friction = 0f; f})

    body.setUserData(sprite)
    fixture.setUserData(this)
    GameLoader.toolDb += this
    GameLoader.thingDb += this
  }
}
