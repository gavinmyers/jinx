package game

import scala.collection.mutable

trait Tool extends Thing {
  var active:Boolean = false

  def use() = {
    this.active = this.active == false
  }
}
