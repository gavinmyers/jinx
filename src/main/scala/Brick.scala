import com.badlogic.gdx.graphics.g2d.{Sprite, TextureRegion}
import com.badlogic.gdx.physics.box2d._
import scala.collection.mutable.ListBuffer

class Brick(item_name:String, world:World, texture:TextureRegion, posX:Float, posY:Float)
  extends Thing() {

  this.name = item_name

  bodyDef = new BodyDef()

  bodyDef.`type` = BodyDef.BodyType.StaticBody
  bodyDef.fixedRotation = true
  bodyDef.position.set(GameUtil.pixelsToMeters(posX), GameUtil.pixelsToMeters(posY))

  fixtureDef = new FixtureDef()

  shape = new PolygonShape()

  fixtureDef.shape = shape
  fixtureDef.friction = 5f


  sprite = new Sprite(texture)
  shape.asInstanceOf[PolygonShape].setAsBox(GameUtil.pixelsToMeters(sprite.getHeight / 2), GameUtil.pixelsToMeters(sprite.getWidth / 2))


  body = world.createBody(bodyDef)

  fixture = body.createFixture(fixtureDef)

  var f:Filter = new Filter()
  f.groupIndex = 2
  fixture.setFilterData(f)
  body.setUserData(sprite)

  body.setUserData(sprite)
  fixture.setUserData(this)
  GameLoader.groundDb += name -> this
  GameLoader.thingDb += this

  override def contact(thing:Thing) : Unit = {

  }

  override def damage(source:Thing, amount:Integer): Unit = {

  }
}

