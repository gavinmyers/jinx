import com.badlogic.gdx.graphics.g2d.TextureRegion

import scala.collection.mutable.ListBuffer

class Weapon {


  var lastAttack:Float= 0
  var cooldown:Float = 0.3f
  var attacking:Boolean = false
  var controller:Being = _

  var bulletSheet:ListBuffer[TextureRegion] = _

  def update(gameTime:Float): Unit = {
    if(lastAttack + cooldown < gameTime) {
      attacking = false
    }
  }

  def attack(gameTime:Float): Unit = {
    if(lastAttack + cooldown > gameTime) {
      return
    }
    attacking = true
    lastAttack = gameTime

    var x = controller.sprite.getX
    if(controller.face_h.equalsIgnoreCase("R")) {
      x += (controller.width)
    }
    val y = controller.sprite.getY + (controller.height / 2)

    val b: Bullet = new Bullet("bullet_" + Math.random(), GameLoader.world, bulletSheet, x, y)
    b.mov_h = controller.face_h
    b.attacker = controller

  }

}
