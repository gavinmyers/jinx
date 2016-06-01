package old

import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.physics.box2d._

class Exit(name:String, destination:String, target:String, room:Room, posX:Float, posY:Float, width:Float, height:Float) extends Thing(room:Room) {
  var fixture: Fixture = _

  override def init(): Unit = {
    this.sprite = new Sprite(Lilac.sheetTextures.head)

    this.body = room.world
      .createBody(
        {
          val b: BodyDef = new BodyDef()
          b.`type` = BodyDef.BodyType.StaticBody
          b.fixedRotation = true
          b.position.set(GameUtil.pixelsToMeters(posX), GameUtil.pixelsToMeters(posY))
          b
        })

    this.fixture = body.createFixture(
      {
        val f:FixtureDef = new FixtureDef()
        var shape:PolygonShape = new PolygonShape()
        f.isSensor = true
        f.shape = shape
        shape.setAsBox(GameUtil.pixelsToMeters(height / 2f), GameUtil.pixelsToMeters(width / 2f))
        f.friction = 0f
        f
      })

    this.sprite.setAlpha(0f)

    body.setUserData(sprite)
    fixture.setUserData(this)
    room.thingDb += this
  }

  override def contact(thing:Thing) : Unit = {
    if (GameLoader.player != null) {
      def player = GameLoader.player
      if(thing == player) {
        GameLoader.goto = this.destination
        GameLoader.target = this.target
      }
    }
  }
}
