package game


class Notification extends Thing {
  println("hi")
  var message:String = ""
  var target:Thing = _
  var showing:Boolean = false
  var showDuration:Float = 2f
  var created:Float = 0f
  this.category = Thing.notification
  var cooldown:Float = 0.4f

  override def update(gameTime:Float): Unit = {
    if(created + cooldown < gameTime) {
      this.die()
    }
  }
}
