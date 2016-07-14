package game


class Notification(message:String) extends Thing {
  var showing:Boolean = false
  var showDuration:Float = 2f
  var created:Float = 0f
}
