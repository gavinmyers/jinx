package game

import scala.collection.mutable

trait Creature extends Thing {
  def healthMax: Float

  def healthCurrent: Float

  def hungerMax: Float

  def hungerCurrent: Float

  var movH: String = ""
  var faceH: String = ""
  var movV: String = ""
  var faceV: String = ""

  var jump: Boolean = false
  var jumping: Boolean = false
  var jumpMax: Float = 0.45f
  var lastJump: Float = 0

  var takingDamage: Boolean = false
  var lastDamage: Float = 0
  var damageCooldown: Float = 0.3f

  var dieing: Boolean = false
  var deathStart: Float = 0
  var deathEnd: Float = 0.3f

  var canFly: Boolean = false

  var jumpMaxVelocity: Float = 15f
  var runMaxVelocity: Float = 2.5f

  def moveRight() = {
    movH = "R"
    faceH = "R"
  }

  def moveLeft() = {
    movH = "L"
    faceH = "L"
  }

  def moveUp() = {
    movV = "U"
  }

  def moveDown() = {
    movV = "D"
  }

  def stop() = {
    movH = ""
  }
}


class GenericCreature(
                       var id: String = java.util.UUID.randomUUID.toString,
                       var location: Thing = null,
                       var destroyed: Boolean = false,
                       var startX: Float = 1.0f,
                       var startY: Float = 1.0f,
                       var height: Float = 100f,
                       var width: Float = 100f,
                       var healthCurrent: Float = 100f,
                       var healthMax: Float = 100f,
                       var hungerCurrent: Float = 100f,
                       var hungerMax: Float = 100f,
                       var scaleX: Float = 1.0f,
                       var scaleY: Float = 1.0f,
                       var size: Float = 10f,
                       var weight: Float = 1f,
                       var category: Short = Thing.nothing,
                       var description: String = "Some scary monster, booo!",
                       var inventory: mutable.Map[String, Thing] = scala.collection.mutable.Map[String, Thing]())
  extends Creature {


}
