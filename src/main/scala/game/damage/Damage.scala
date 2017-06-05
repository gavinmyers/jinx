package game.damage

import game.Attribute.Attribute
import game.{Creature, Thing}

trait Damage extends java.io.Serializable {
  var id:String = ""
  var base:Float = 0f
  var mod:Attribute = null
  def calculate(attacker:Thing, defender:Thing):Float = {
    var b:Float = base

    if(mod != null && attacker != null && attacker.attributes != null && attacker.get(this.mod) != null){
      var m:Float = attacker.get(this.mod).current
      b += m
    }

    if(defender != null && defender.resistences != null && defender.resistences.contains(id)) {
      val res:Damage = defender.resistences(id)
      var db:Float = res.base
      if(res.mod != null && defender.attributes != null && defender.get(res.mod) != null) {
        db += defender.get(res.mod).current
      }
      b = Math.max(0, base - db)
    }
    return b
  }
}
