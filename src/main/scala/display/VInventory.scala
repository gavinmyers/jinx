package display

import box2dLight.{PointLight, PositionalLight, RayHandler}
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.{Texture, Color, GL20, OrthographicCamera}
import com.badlogic.gdx.graphics.g2d.{SpriteBatch, TextureRegion}
import com.badlogic.gdx.maps.MapObject
import com.badlogic.gdx.maps.objects.RectangleMapObject
import com.badlogic.gdx.math.{Matrix4, Vector3, Vector2}
import com.badlogic.gdx.physics.box2d.{Box2DDebugRenderer, World}
import game.{Thing, Creature}
import utils.Conversion

import scala.collection.mutable.ListBuffer
import scala.collection.JavaConversions._

object VInventory {
  def sheet = new Texture("gui.png")
  val sheetTextures:ListBuffer[TextureRegion] = ListBuffer()
  for(tr <- TextureRegion.split(sheet, 24, 24)) {
    for(tx <- tr) {
      sheetTextures.append(tx)
    }
  }
}


class VInventory (container:Thing) {
  val camera:OrthographicCamera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight())

  val debugRenderer: Box2DDebugRenderer = new Box2DDebugRenderer()
  val world: World = new World(new Vector2(0, -75f), true)

  val handler:RayHandler = new RayHandler(world)
  this.handler.setAmbientLight(1f, 1f, 1f, 1f)

  val batch: SpriteBatch = new SpriteBatch()

  var health:VStatus = _
  var hunger:VStatus = _
  var weapon:VStatus = _

  var vinventory:scala.collection.mutable.Map[String,VThing] = {
    val ret:scala.collection.mutable.Map[String,VThing] = scala.collection.mutable.Map[String,VThing]()
    for((k,thing) <- container.inventory) {
      ret += thing.id -> VThing.create(thing, world)
    }
    ret
  }

  def render():Unit = {
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
    camera.zoom = 0.5f
    camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight())
    camera.update()

    if(health == null) {
      health = new VStatus(Gdx.graphics.getWidth() * camera.zoom / 2f, Gdx.graphics.getHeight() * camera.zoom - 24f, world, VInventory.sheetTextures(9))
      hunger = new VStatus(Gdx.graphics.getWidth() * camera.zoom / 2f, Gdx.graphics.getHeight() * camera.zoom - 24f, world, VInventory.sheetTextures(10))
      weapon = new VStatus(Gdx.graphics.getWidth() * camera.zoom / 2f, Gdx.graphics.getHeight() * camera.zoom - 24f, world, VInventory.sheetTextures(11))
    }

    handler.setCombinedMatrix(camera)
    batch.setProjectionMatrix(camera.combined)
    batch.begin()
    var sX:Float = 0
    var sY:Float = (Gdx.graphics.getHeight() * camera.zoom) - 24f
    var step:Float = 24f
    for((k,thing) <- container.inventory) {
      if(vinventory.contains(k) == false) {
        vinventory += thing.id -> VThing.create(thing, world)
      }
      if(sX / step > 7) {
        sY -= step
        sX = 0
      }
      val fet:VThing = vinventory(k)
      fet.body.setTransform(Conversion.pixelsToMeters(thing.transformX + sX + 24f), Conversion.pixelsToMeters(thing.transformY + sY ), 0f)
      fet.sprite.setPosition(Conversion.metersToPixels(fet.body.getPosition.x) - fet.sprite.getWidth/2 , Conversion.metersToPixels(fet.body.getPosition.y) - fet.sprite.getHeight/2 )
      fet.sprite.draw(batch)
      sX += step
    }

    if(container.isInstanceOf[Creature]) {
      val creature:Creature = container.asInstanceOf[Creature]
      sX = (Gdx.graphics.getWidth() * camera.zoom) / 2f
      sY = (Gdx.graphics.getHeight() * camera.zoom) - 24f
      health.body.setTransform(Conversion.pixelsToMeters(sX + 24f), Conversion.pixelsToMeters(sY), 0f)
      health.sprite.setPosition(Conversion.metersToPixels(health.body.getPosition.x) - health.sprite.getWidth/2 , Conversion.metersToPixels(health.body.getPosition.y) - health.sprite.getHeight/2 )
      health.sprite.draw(batch)

      sY -= 24f
      hunger.body.setTransform(Conversion.pixelsToMeters(sX + 24f), Conversion.pixelsToMeters(sY), 0f)
      hunger.sprite.setPosition(Conversion.metersToPixels(hunger.body.getPosition.x) - hunger.sprite.getWidth/2 , Conversion.metersToPixels(hunger.body.getPosition.y) - hunger.sprite.getHeight/2 )
      hunger.sprite.draw(batch)

      sY -= 24f
      weapon.body.setTransform(Conversion.pixelsToMeters(sX + 24f), Conversion.pixelsToMeters(sY), 0f)
      weapon.sprite.setPosition(Conversion.metersToPixels(weapon.body.getPosition.x) - weapon.sprite.getWidth/2 , Conversion.metersToPixels(weapon.body.getPosition.y) - weapon.sprite.getHeight/2 )
      weapon.sprite.draw(batch)
      if(creature.holding != null) {
        val fet:VThing = vinventory(creature.holding.id)
        fet.body.setTransform(Conversion.pixelsToMeters(sX + 48f), Conversion.pixelsToMeters(sY), 0f)
        fet.sprite.setPosition(Conversion.metersToPixels(fet.body.getPosition.x) - fet.sprite.getWidth/2 , Conversion.metersToPixels(fet.body.getPosition.y) - fet.sprite.getHeight/2 )
        fet.sprite.draw(batch)
      }
    }

    batch.end()

    def debugMatrix: Matrix4 = batch.getProjectionMatrix.cpy().scale(Conversion.BOX_TO_WORLD, Conversion.BOX_TO_WORLD, 0f)
    handler.setCombinedMatrix(debugMatrix)
    //debugRenderer.render(world, debugMatrix)
    handler.updateAndRender()
  }

}
