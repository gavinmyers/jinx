import box2dLight.{PositionalLight, PointLight, RayHandler}
import com.badlogic.gdx.graphics.{OrthographicCamera, Color, Texture}
import com.badlogic.gdx.graphics.g2d.{TextureRegion, BitmapFont, SpriteBatch}
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.maps.tiled.{TiledMapTile, TiledMapTileLayer, TmxMapLoader, TiledMap}
import com.badlogic.gdx.math.{Matrix4, Vector2}
import com.badlogic.gdx.physics.box2d.{JointEdge, BodyDef, Box2DDebugRenderer, World}
import scala.collection.JavaConversions._

import scala.collection.mutable.ListBuffer

object GameLoader {
  def BOX_TO_WORLD = 32f
  var gameTime:Float = 0

  var goto:String = ""
  var target:String = ""

  var room:Room = _

  var player:Being = _
  var roomDb:scala.collection.mutable.Map[String,Room] = scala.collection.mutable.Map[String,Room]()

}

object GameUtil {

  def pixelsToMeters(v:Float):Float = {
    v / GameLoader.BOX_TO_WORLD
  }

  def metersToPixels(v:Float):Float = {
    v * GameLoader.BOX_TO_WORLD
  }
}
