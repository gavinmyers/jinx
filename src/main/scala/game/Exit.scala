package game

import scala.collection.mutable

class Exit
  extends Thing {
  this.category = Thing.exit
  var destination:Thing = _
  var entrance:Thing = _
}

