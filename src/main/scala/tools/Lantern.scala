package tools

import game.{Tool}


class Lantern extends Tool {
  this.brightness = 2
  this.luminance = 1f

  override def update(gameTime:Float): Unit = {
    if(this.location != null) {
      this.location.luminance = 0.4f
      this.location.brightness = 3f + Math.random().toFloat
    }
    this.brightness = 2f + Math.random().toFloat
    println(this.brightness)
  }
}
