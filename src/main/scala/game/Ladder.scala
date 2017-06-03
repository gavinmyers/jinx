package game

class Ladder extends Thing {
  this.category = Thing.ladder
  set("friction",new MaxCurrentMin(0f,0f,0f))
  this.description = "A ladder"
  this.platform = true
}
