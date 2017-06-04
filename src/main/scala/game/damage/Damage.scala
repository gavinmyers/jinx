package game.damage

import game.{Creature, Thing}

trait Damage extends java.io.Serializable {
  var id:String = ""
  var base:Float = 0f
  var mod:String = ""
  def calculate(attacker:Thing, defender:Thing):Float = {
    var b:Float = base
    if(attacker != null && attacker.attributes != null && attacker.attributes.contains(this.mod)){
      var m:Float = attacker.get(this.mod).current
      b += m
    }
    if(defender != null && defender.resistences != null && defender.resistences.contains(id)) {
      var res:Damage = defender.resistences(id)
      var db:Float = res.base
      if(defender.attributes != null && defender.attributes.contains(res.mod)) {
        db += defender.get(res.mod).current
      }
      b = Math.max(0, base - db)
    }
    return b
  }
}
