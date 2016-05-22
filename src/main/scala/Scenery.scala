import javax.sound.midi.Receiver

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.{Animation, Sprite, TextureRegion, Batch}
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d._

import scala.collection.mutable.ListBuffer

object Scenery {
  def sheet = new Texture("bullet1.png")
  val sheetTextures:ListBuffer[TextureRegion] = ListBuffer()
  for(tr <- TextureRegion.split(sheet, 24, 24)) {
    for(tx <- tr) {
      sheetTextures.append(tx)
    }
  }
}

class Scenery(name:String, room:Room, posX:Float, posY:Float) extends Thing(room:Room) {

  this.created = GameLoader.gameTime
  var fixture: Fixture = _
  def animationSheet = Scenery.sheetTextures

  //
  var sceneryAnimation:Animation = new Animation(Math.random().toFloat / 4f + 0.15f, animationSheet(36),animationSheet(37),animationSheet(38),animationSheet(39))

  override def init(): Unit = {
    this.sprite = new Sprite(animationSheet.head)
    this.sprite.setScale(scaleX, scaleY)

    var width = sprite.getWidth * scaleX
    var height = sprite.getHeight * scaleY

    this.body = room.world
      .createBody(
        {val b: BodyDef = new BodyDef()
          b.`type` = BodyDef.BodyType.StaticBody
          b.fixedRotation = true
          b.position.set(GameUtil.pixelsToMeters(posX), GameUtil.pixelsToMeters(posY))
          b})

    this.fixture = body.createFixture(
      {val f:FixtureDef = new FixtureDef()
        var shape:PolygonShape = new PolygonShape()
        f.isSensor = true
        f.shape = shape
        shape.setAsBox(GameUtil.pixelsToMeters(height / 2f), GameUtil.pixelsToMeters(width / 2f))
        f.friction = 0f; f})

    body.setUserData(sprite)
    fixture.setUserData(this)

    room.sceneryDb += this
  }


  var moving:Boolean = true
  override def move(gameTime:Float): Unit = {
    this.sprite.setRegion(sceneryAnimation.getKeyFrame(gameTime, true))
  }

  override def draw(batch:Batch): Unit = {
    super.draw(batch)
  }

}
