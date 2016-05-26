class Lantern(room:Room, posX:Float, posY:Float) extends Tool(room:Room, Tool.sheetTextures(13), posX:Float, posY:Float) {
  override def init(): Unit = {
    this.luminance = 0.4f
    super.init()
  }
}