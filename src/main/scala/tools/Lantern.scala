package tools

import game.{Tool}


class Lantern extends Tool {
  this.brightness = 2
  this.luminance = 1f
  this.category = game.Thing.lantern

  override def update(gameTime:Float): Unit = {

    if(this.location != null) {
      if(this.active) {
        this.location.luminance = 0.4f
        this.location.brightness = 3f + Math.random().toFloat
      } else {
        this.location.luminance = 0f
        this.location.brightness = 0f
      }
    }
    this.brightness = 2f + Math.random().toFloat
  }
}
