package old

class Claw(controller:Being) extends Weapon(controller:Being) {
  cooldown = 0.25f

  override def fire() :Unit = {
    def xy = attackXY()
    val b: Bullet = new Swipe("bullet_" + Math.random(), controller.location, Weapon.sheetTextures, xy._1, xy._2, controller.scaleX, controller.scaleY)
    b.mov_h = controller.face_h
    b.attacker = controller
  }

}
