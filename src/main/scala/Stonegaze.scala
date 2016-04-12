class Stonegaze(controller:Being) extends Weapon(controller:Being) {

  cooldown = 1.5f


  override def attack(gameTime:Float): Unit = {
    if(lastAttack + cooldown > gameTime) {
      return
    }
    attacking = true
    lastAttack = gameTime

    var x = controller.sprite.getX
    if(controller.face_h.equalsIgnoreCase("R")) {
      x += controller.width
    }
    val y = controller.sprite.getY + (controller.height / 2)

    val xr:Float = (Math.random() * controller.sprite.getWidth).toFloat
    val yr:Float = (Math.random() * controller.sprite.getHeight).toFloat
    val b: Bullet = new Stonebeam("bullet_" + Math.random(), GameLoader.world, Weapon.sheetTextures, x + xr, y + yr, controller.scaleX, controller.scaleY)
    b.mov_h = controller.face_h
    b.attacker = controller

  }

}
