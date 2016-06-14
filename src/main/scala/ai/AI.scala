package ai

import game.{Creature, Room, Thing}

trait AI {
  var friends:Short = AI.nobody
  var enemies:Short = AI.nobody
  var alignment:Short = AI.neutral
  var sentient:Boolean = true
  var lastThought:Float = 0
  var thoughtCooldown = 2f
  var lastAttack:Float = 0
  var attackCooldown:Float = 2f
  def think(gameTime:Float, thinker:Thing): Unit = {
    if(!thinker.isInstanceOf[Creature]) return

    if(!sentient) return

    if(!thinker.location.isInstanceOf[Room]) return

    if(lastThought + thoughtCooldown > gameTime) return


    lastThought = gameTime
    //Find the nearest enemy
    val enemy:Thing = AI.findEnemy(this, thinker.location.asInstanceOf[Room])

    if(null == enemy) return

    //Approach them
    if(enemy.lastX > thinker.lastX) {
      thinker.asInstanceOf[Creature].moveRight()
    } else {
      thinker.asInstanceOf[Creature].moveLeft()
    }

    //ATTACK!
    def cx:Float = enemy.lastX - thinker.lastX
    def cy:Float = enemy.lastY - thinker.lastY
    if(cx > (enemy.width * -2) && cx < (enemy.width * 2) && cy > (enemy.width * -2) && cy < (enemy.width * 2)) {
      thinker.asInstanceOf[Creature].attack(gameTime)
    }


  }
}

class PlayerAI extends AI {
  this.sentient = false
  this.friends = AI.foxes
  this.enemies = AI.evil
  this.alignment = AI.good
}

class GenericAI extends AI {
  this.sentient = true
  this.friends = AI.evil
  this.enemies = AI.good
  this.alignment = AI.evil
}

object AI {
  def nobody: Short = 0x00
  def foxes:Short = 0x02
  def evil:Short = 0xA1
  def good:Short = 0xA2
  def neutral:Short = 0xA3

  def findEnemy(thought:AI, room:Room):Thing = {
    for((k,thing) <- room.inventory) {
      if(thing.ai != null) {
        if(thing.ai.alignment == thought.enemies || thing.ai.friends == thought.enemies) {
          return thing
        }
      }
    }
    return null
  }
}


