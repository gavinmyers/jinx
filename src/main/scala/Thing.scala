import box2dLight.{PointLight, PositionalLight, RayHandler}
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.{Animation, Batch, Sprite, TextureRegion}
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d._
import net.dermetfan.gdx.graphics.g2d.Box2DSprite
import scala.collection.JavaConversions._
import scala.collection.mutable.ListBuffer

class Thing {
  var name:String = ""
  var created:Float = 0
  var mov_h:String = ""
  var mov_v:String = ""
  var scaleX:Float = 1.0f
  var scaleY:Float = 1.0f
  var face_v:String = ""
  var face_h:String = ""
  var sprite:Sprite = _
  var body:Body = _
  var destroyed:Boolean = false
  var effectDb:ListBuffer[Effect] = ListBuffer()

  init()

  def draw(batch:Batch): Unit = {
    sprite.setPosition(GameUtil.metersToPixels(body.getPosition.x) - sprite.getWidth/2 , GameUtil.metersToPixels(body.getPosition.y) - sprite.getHeight/2 )
    sprite.draw(batch)
  }

  def destroy() : Unit = {
    destroyed = true
  }

  def contact(thing:Thing) : Unit = {
    //do nothing
  }

  def damage(source:Bullet, amount:Float): Unit = {

  }

  def damage(source:Effect, amount:Float): Unit = {

  }

  def update(gameTime:Float): Unit = {

  }

  def move(gameTime:Float): Unit = {

  }

  def init(): Unit = {

  }

  def apply(effect:Effect): Unit = {
    for(e:Effect <- this.effectDb) {
      if(effect.opposite != null && e.getClass.equals(effect.opposite)) {
        e.shrink()
        effect.destroy()
        return
      } else if(e.getClass.equals(effect.getClass)) {
        e.grow()
        effect.destroy()
        return
      }
    }
    effectDb += effect
  }

  def cease(effect:Effect): Unit = {
    effectDb -= effect
  }

  def petrify(): Unit = {

  }

}






