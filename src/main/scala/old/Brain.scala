package old

class Brain(controller:Being) {

  var lastThought:Float= 0
  var cooldown:Float = 0.5f + (Math.random() * 2).toFloat
  var thinking:Boolean = false

  def update(gameTime:Float): Unit = {
    if(GameLoader.player == null) return

    if(lastThought + cooldown < gameTime) {
      thinking = false
      think(gameTime)
    }
  }

  def think(gameTime:Float): Unit = {
    if(lastThought + cooldown > gameTime) {
      return
    }
    thinking = true
    lastThought = gameTime


    def player = GameLoader.player
    if(player.body.getPosition.x > controller.body.getPosition.x) {
      controller.moveRight(gameTime)
    } else {
      controller.moveLeft(gameTime)
    }

    def cx:Float = player.body.getPosition.x - controller.body.getPosition.x
    def cy:Float = player.body.getPosition.y - controller.body.getPosition.y
    if(cx > -1 && cx < 1 && cy > -1 && cy < 1) {
      controller.attack(gameTime)
    }
    if(controller.weapon.lastAttack + (controller.weapon.cooldown * 2) < gameTime) {
      controller.attack(gameTime)
    }
    /*

    val body = GameLoader.world
      .createBody(
        {val b: BodyDef = new BodyDef()
          b.`type` = BodyDef.BodyType.DynamicBody
          b.fixedRotation = true
          b.gravityScale = 0
          b.position.set(controller.body.getPosition.x, controller.body.getPosition.y)
          b})

    val fixture = body.createFixture(
        {val f:FixtureDef = new FixtureDef()
          var shape:Shape = new CircleShape()
          f.shape = shape
          f.isSensor = true
          shape.setRadius(GameUtil.pixelsToMeters(controller.height / 4.2f))
          f.friction = 0f; f})
          */

  }

}
