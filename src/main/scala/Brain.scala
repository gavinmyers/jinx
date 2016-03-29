class Brain(controller:Being) {

  var lastThought:Float= 0
  var cooldown:Float = 3.3f
  var thinking:Boolean = false

  def update(gameTime:Float): Unit = {
    if(lastThought + cooldown < gameTime) {
      thinking = false
    }
  }

  def think(gameTime:Float): Unit = {
    if(lastThought + cooldown > gameTime) {
      return
    }
    thinking = true
    lastThought = gameTime
    
  }

}
