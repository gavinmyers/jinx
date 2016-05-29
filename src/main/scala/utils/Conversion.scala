package utils


object Conversion {
  def BOX_TO_WORLD = 32f

  def pixelsToMeters(v:Float):Float = {
    v / BOX_TO_WORLD
  }

  def metersToPixels(v:Float):Float = {
    v * BOX_TO_WORLD
  }
}


