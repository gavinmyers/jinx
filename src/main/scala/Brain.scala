import com.badlogic.gdx.physics.box2d.{CircleShape, Shape, FixtureDef, BodyDef}

class Brain(controller:Being) {

  var lastThought:Float= 0
  var cooldown:Float = 0.5f
  var thinking:Boolean = false

  def update(gameTime:Float): Unit = {
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
    def player = GameLoader.monsterDb("player")
    if(player.body.getPosition.x > controller.body.getPosition.x) {
      controller.moveRight(GameLoader.gameTime)
    } else {
      controller.moveLeft(GameLoader.gameTime)
    }

    def c:Float = player.body.getPosition.x - controller.body.getPosition.x
    if(c > -1 && c < 1) {
      controller.attack(GameLoader.gameTime)
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
