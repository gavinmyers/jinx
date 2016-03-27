import box2dLight.{PointLight, PositionalLight, RayHandler}
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.{Animation, Batch, Sprite, TextureRegion}
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d._
import net.dermetfan.gdx.graphics.g2d.Box2DSprite
import scala.collection.JavaConversions._
import scala.collection.mutable.ListBuffer

class Thing() {


  var name:String = _

  var created:Float = GameLoader.gameTime

  var sprite:Sprite = _
  var fixtureDefBottom:FixtureDef = _
  var fixtureDefTop:FixtureDef = _
  var fixtureDefRight:FixtureDef = _
  var fixtureDefLeft:FixtureDef = _
  var fixtureDef:FixtureDef = _

  var fixtureBottom:Fixture = _
  var fixtureTop:Fixture = _
  var fixtureRight:Fixture = _
  var fixtureLeft:Fixture = _
  var fixture:Fixture = _

  var bodyDef:BodyDef = _
  var body:Body = _
  var shape:Shape = _

  var mov_h:String = ""
  var mov_v:String = ""
  var face_v:String = ""
  var face_h:String = ""


  def draw(batch:Batch): Unit = {
    sprite.setPosition(GameUtil.metersToPixels(body.getPosition().x) - sprite.getWidth()/2 , GameUtil.metersToPixels(body.getPosition().y) - sprite.getHeight()/2 )
    sprite.draw(batch)
  }

  def destroy() : Unit = {
    GameLoader.thingDb -= this
  }

  def contact(thing:Thing) : Unit = {
    //do nothing
  }

  def damage(source:Thing, amount:Integer): Unit = {

  }

  def update(gameTime:Float): Unit = {

  }

  def move(gameTime:Float): Unit = {

  }

}






