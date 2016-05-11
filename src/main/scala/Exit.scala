import com.badlogic.gdx.graphics.g2d.{Sprite, TextureRegion}
import com.badlogic.gdx.physics.box2d._

import scala.collection.mutable.ListBuffer

class Exit(name:String, destination:String, target:String, world:World, posX:Float, posY:Float, width:Float, height:Float) extends Thing() {
  var fixture: Fixture = _

  override def init(): Unit = {
    this.sprite = new Sprite(Lilac.sheetTextures.head)

    this.body = world
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
    GameLoader.thingDb += this
  }

  override def contact(thing:Thing) : Unit = {
    if (GameLoader.monsterDb.contains("player")) {
      def player = GameLoader.monsterDb("player")
      if(thing == player) {
        GameLoader.goto = this.destination
        GameLoader.target = this.target
      }
    }
  }
}
