package game

class Bullet extends Thing {
  var cooldown:Float = 0.4f
  var created:Float = 0f
  var bind:Thing = _
  var movH: String = ""
  var faceH: String = ""
  var movV: String = ""
  var faceV: String = ""
  var speed:Float = 0f

  this.category = Thing.bullet
  override def update(gameTime:Float): Unit = {
    if(created + cooldown < gameTime) {
      this.location.inventory -= this.id
      return
    }
    if(this.bind != null) {
      if(faceH == "R") {
        this.transformX = this.bind.lastX + 18f
        this.transformY = this.bind.lastY
      } else {
        this.transformX = this.bind.lastX - 18f
        this.transformY = this.bind.lastY
      }
    }
  }
}
