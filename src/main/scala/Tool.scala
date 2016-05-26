import box2dLight.PointLight
import com.badlogic.gdx.graphics.{Color, Texture}
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


class Tool(room:Room, texture:TextureRegion, posX:Float, posY:Float) extends Thing(room:Room) {
  var fixture: Fixture = _

  override def init(): Unit = {
    this.sprite = new Sprite(texture)

    var width = sprite.getWidth
    var height = sprite.getHeight

    this.body = room.world
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
        shape.setAsBox(GameUtil.pixelsToMeters(width / 3f), GameUtil.pixelsToMeters(height / 3f))
        f.friction = 0f; f})

    body.setUserData(sprite)
    fixture.setUserData(this)

    if(luminance > 0f) {
      light = new PointLight(location.handler, 24, new Color(1f, 1f, 1f, luminance), 4, 0, 0)
      light.attachToBody(body, 0, 0)
      light.setIgnoreAttachedBody(true)
      //light.setContactFilter(0, 2, -1)
    }

    room.toolDb += this
    room.thingDb += this
  }
}
