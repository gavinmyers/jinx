package tools

import game.{NoAttack, NoUse, Tool}
import logic.Messaging

class Corpse extends Tool with NoAttack with NoUse {
  this.category = game.Thing.corpse
  this.vanishOnDeath = false
  this.description = "a pile of bones"
}
