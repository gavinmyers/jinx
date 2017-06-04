package game.damage

import game.{Creature, Thing}

trait Damage {
  var id:String = ""
  var base:Float = 0f
  def calculate(thing:Thing):Float = {
    if(thing.resistences.contains(id)) {
      var res:Damage = thing.resistences.get(id).get
      return Math.max(0, base - res.base)
    } else {
      return base
    }

  }
}
