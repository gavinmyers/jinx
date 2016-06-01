package old

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion

import scala.collection.mutable.ListBuffer

object Weapon {
  def sheet = new Texture("bullet1.png")
  val sheetTextures:ListBuffer[TextureRegion] = ListBuffer()
  for(tr <- TextureRegion.split(sheet, 24, 24)) {
    for(tx <- tr) {
      sheetTextures.append(tx)
    }
  }
}

class Weapon(controller:Being) extends Thing(controller.location) {
  var lastAttack:Float= 0
  var cooldown:Float = 0.3f
  var attacking:Boolean = false

  override def update(gameTime:Float): Unit = {
    if(lastAttack + cooldown < gameTime) {
      attacking = false
    }
  }

  def attackXY():Tuple2[Float,Float] = {

    var x = controller.sprite.getX
    if(controller.mov_h.equalsIgnoreCase("") && (controller.mov_v.equalsIgnoreCase("U") || controller.mov_v.equalsIgnoreCase("D"))) {
      x += (controller.width / 2)
    } else if(controller.face_h.equalsIgnoreCase("R")) {
      x += controller.width
    }

    var y = controller.sprite.getY + (controller.height / 2)
    if(controller.mov_v.equalsIgnoreCase("U")) {
      y += controller.height
    } else if(controller.mov_v.equalsIgnoreCase("D")) {
      y -= controller.height
    }
    return (x,y)
  }

  def attack(gameTime:Float): Unit = {
    if(lastAttack + cooldown > gameTime) {
      return
    }
    attacking = true
    lastAttack = gameTime
    fire()
  }

  def fire() :Unit = {
    def xy = attackXY()
    val b: Bullet = new Bullet("bullet_" + Math.random(), controller.location, Weapon.sheetTextures, xy._1, xy._2, controller.scaleX, controller.scaleY)
    b.mov_h = controller.face_h
    b.attacker = controller
  }

}
