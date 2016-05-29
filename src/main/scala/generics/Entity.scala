package generics

import com.badlogic.gdx.graphics.g2d.{Batch, TextureRegion, Sprite}
import com.badlogic.gdx.physics.box2d.{Body, World}
import utils.Conversion

import scala.collection.mutable

object Entity {
  def nothing: Short = 0x1234
  def floor: Short = 0x0001
  def creature: Short = 0x0002
  def item: Short = 0x0003
  def bullet: Short = 0x004
}

class Entity(
              var id: String = java.util.UUID.randomUUID.toString,
              var description: String = "",
              var location: Thing = null,
              var destroyed: Boolean = false,
              var category: Short = Entity.nothing,
              var posX: Float = 1.0f,
              var posY: Float = 1.0f,
              var height: Float = 100f,
              var width: Float = 100f,
              var size: Float = 100f,
              var weight: Float = 10f,
              var scaleX: Float = 1.0f,
              var scaleY: Float = 1.0f,
              var inventory: mutable.Map[String, Thing] = scala.collection.mutable.Map[String, Thing]())
  extends Thing {

}
