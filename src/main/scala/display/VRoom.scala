package display

import box2dLight.{PointLight, PositionalLight, RayHandler}
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.{BitmapFont, SpriteBatch}
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter
import com.badlogic.gdx.graphics.{Color, GL20, OrthographicCamera}
import com.badlogic.gdx.maps.MapObject
import com.badlogic.gdx.maps.objects.RectangleMapObject
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.maps.tiled.{TiledMap, TiledMapTile, TiledMapTileLayer, TmxMapLoader}
import com.badlogic.gdx.math.{Matrix4, Vector2, Vector3}
import com.badlogic.gdx.physics.box2d.{Box2DDebugRenderer, JointEdge, World}
import com.badlogic.gdx.utils.Align
import game._
import utils.Conversion

import scala.collection.JavaConversions._


class VRoom(map:String, room:Room) {
  val tiles:TiledMap = new TmxMapLoader().load(map + ".tmx")
  val tileRenderer:OrthogonalTiledMapRenderer = new OrthogonalTiledMapRenderer(tiles)

  val camera:OrthographicCamera = new OrthographicCamera()
  val parallalaxCameras:Array[OrthographicCamera] = Array.fill[OrthographicCamera](10)(new OrthographicCamera())

  val debugRenderer: Box2DDebugRenderer = new Box2DDebugRenderer()
  val world: World = new World(new Vector2(0, -75f), true)

  val handler:RayHandler = new RayHandler(world)


  val batch: SpriteBatch = new SpriteBatch()

  val cameraStart: Vector3 = {
    val res:Vector3 = new Vector3()
    for (mo: MapObject <- tiles.getLayers.get("positions").getObjects) {
      if ("target".equalsIgnoreCase(mo.getName)) {
        if (mo.getProperties.get("default") != null) {
          def r = mo.asInstanceOf[RectangleMapObject].getRectangle
          res.x = r.x
          res.y = r.y
        }
      }
    }
    res
  }


  val descriptionFont:BitmapFont = {
    val generator:FreeTypeFontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("VT323-Regular.ttf"))
    val parameter:FreeTypeFontParameter = new FreeTypeFontParameter()
    parameter.size = 22
    parameter.color = Color.LIGHT_GRAY
    generator.generateFont(parameter)
  }

  val menuFont:BitmapFont = {
    val generator:FreeTypeFontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("VT323-Regular.ttf"))
    val parameter:FreeTypeFontParameter = new FreeTypeFontParameter()
    parameter.size = 22
    parameter.color = Color.WHITE
    generator.generateFont(parameter)
  }

  val statsFont:BitmapFont = {
    val generator:FreeTypeFontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("VT323-Regular.ttf"))
    val parameter:FreeTypeFontParameter = new FreeTypeFontParameter()
    parameter.size = 22
    parameter.color = Color.CYAN
    generator.generateFont(parameter)
  }

  var vinventory:scala.collection.mutable.Map[String,VThing] = {
    val ret:scala.collection.mutable.Map[String,VThing] = scala.collection.mutable.Map[String,VThing]()
    for((k,thing) <- room.inventory) {
      ret += thing.id -> VThing.create(thing, world)
    }
    ret
  }


  var vnotifications:scala.collection.mutable.Map[String,VNotification] = {
    val ret:scala.collection.mutable.Map[String,VNotification] = scala.collection.mutable.Map[String,VNotification]()
    for((k,thing) <- room.inventory) {
      for((k2,notification) <- thing.notifications) {
        ret += thing.id -> (VThing.create(notification, world).asInstanceOf[VNotification])
      }
    }
    ret
  }

  def renderAlert():Unit = {
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
    handler.setAmbientLight(1f, 1f, 1f, 1f)
    batch.begin()
    def normalProjection:Matrix4 = new Matrix4().setToOrtho2D(0, 0, Gdx.graphics.getWidth(),  Gdx.graphics.getHeight())
    batch.setProjectionMatrix(normalProjection)
    statsFont.draw(batch,room.alert,30,Gdx.graphics.getHeight() / 2,Gdx.graphics.getWidth() - 60,Align.center,true)
    statsFont.draw(batch,"press [spacebar] to continue",30,30,Gdx.graphics.getWidth(),Align.center,true)

    batch.end()
  }

  def render(targetX:Float, targetY:Float, gameTime:Float):Unit = {
    if(room.alert.length > 0) {
      return renderAlert()
    }
    //Gdx.gl.glClearColor(0, 0, 0, 1)
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
    //handler.setAmbientLight(1f, 1f, 1f, 1f)
    val al:Float = room.ambientLight
    handler.setAmbientLight(al, al, al, al)
    handler.setCombinedMatrix(camera)

    camera.zoom = 0.5f
    camera.setToOrtho(false)

    camera.translate(0, 0, 0)
    camera.position.set(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, 0)
    val position: Vector3 = new Vector3(cameraStart.x, cameraStart.y, cameraStart.z)
    //position.x = cameraStart.x
    position.x = targetX
    camera.position.set(position)
    camera.update()

    var spd = 0.1f
    val backgroundPallalaxTmp: Array[Vector3] = Array.fill[Vector3](10)(new Vector3())
    for (i <- 0 to 9) {
      backgroundPallalaxTmp(i).x = cameraStart.x - ((cameraStart.x - targetX) * spd)
      //backgroundPallalaxTmp(i).x = targetX - (targetX * spd)
      backgroundPallalaxTmp(i).y = cameraStart.y
      spd += 0.1f
    }


    world.step(Gdx.graphics.getDeltaTime, 6, 2)
    for((k,vthing) <- vnotifications) {
      if(vthing.destroyed == true) {
        println("it's dead!")
      }
      if(vthing.destroyed) {
        for (je: JointEdge <- vthing.body.getJointList) {
          world.destroyJoint(je.joint)
        }
        world.destroyBody(vthing.body)
        if(vthing.light != null && vthing.light.isActive()) {
          vthing.light.setActive(false)
          vthing.light.dispose()
        }
        vnotifications -= k
      }
    }

    for((k,vthing) <- vinventory) {
      if (room.inventory.containsKey(k) == false) {

        for (je: JointEdge <- vthing.body.getJointList) {
          world.destroyJoint(je.joint)
        }
        world.destroyBody(vthing.body)
        if(vthing.light != null && vthing.light.isActive()) {
          vthing.light.setActive(false)
          vthing.light.dispose()
        }
        vinventory -= k
      }
    }


    for (i <- 0 to 9) {
      val cam:OrthographicCamera = parallalaxCameras(i)
      cam.zoom = 0.5f
      cam.setToOrtho(false)
      cam.translate(0,0,0)
      cam.position.set(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, 0)
      cam.position.set(backgroundPallalaxTmp(i))
      cam.update()

      batch.setProjectionMatrix(cam.combined)
      batch.begin()
      def x: Float = cam.position.x - cam.viewportWidth * cam.zoom
      def y: Float = cam.position.y - cam.viewportHeight * cam.zoom
      def width: Float = cam.viewportWidth * cam.zoom * 2
      def height: Float = cam.viewportHeight * cam.zoom * 4
      tileRenderer.setView(cam.combined, x, y, width, height)
      tileRenderer.render(Array(i))
      batch.end()
    }



    batch.setProjectionMatrix(camera.combined)
    batch.begin()
    for((k,thing) <- room.inventory) {
      if(vinventory.contains(k) == false) {
        vinventory += thing.id -> VThing.create(thing, world)
      }

      val fet:VThing = vinventory(k)
      thing.update(gameTime)
      if(thing.get("luminance") > 0f && fet.light == null) {
        val light:PositionalLight = new PointLight(handler, 24, new Color(1f, 1f, 1f, thing.get("luminance")), thing.get("brightness"), 0, 0)
        light.attachToBody(fet.body, 0, 0)
        light.setIgnoreAttachedBody(true)
        light.setContactFilter(Thing.floor, Thing.floor, Thing.floor)
        light.setActive(true)
        fet.light = light
      }
      if(fet.light != null) {
        fet.light.setDistance(thing.get("brightness"))
      }
      fet.update(gameTime)
      fet.sprite.setPosition(Conversion.metersToPixels(fet.body.getPosition.x) - fet.sprite.getWidth/2 , Conversion.metersToPixels(fet.body.getPosition.y) - fet.sprite.getHeight/2 )
      fet.sprite.draw(batch)

      if(thing.transformX > 0 && thing.transformY > 0) {
        fet.body.setTransform(Conversion.pixelsToMeters(thing.transformX), Conversion.pixelsToMeters(thing.transformY), 0f)
        thing.transformX = 0
        thing.transformY = 0
      }

      for((k2,notification) <- thing.notifications) {

        if(vnotifications.contains(k) == false) {
          vnotifications += k2 -> (VThing.create(notification, world).asInstanceOf[VNotification])
        }
        notification.update(gameTime)
        val fet:VThing = vnotifications(k2)
        fet.destroyed = notification.destroyed
        fet.update(gameTime)
        fet.sprite.setPosition(Conversion.metersToPixels(fet.body.getPosition.x) - fet.sprite.getWidth/2 , Conversion.metersToPixels(fet.body.getPosition.y) - fet.sprite.getHeight/2 )
        fet.sprite.draw(batch)
      }
    }

    batch.end()

    batch.begin()
    tileRenderer.setView(camera)
    tileRenderer.render(Array(13, 14, 15, 16, 17, 18, 19, 20, 21, 22))
    batch.end()


    /*
    def debugMatrix: Matrix4 = batch.getProjectionMatrix.cpy().scale(Conversion.BOX_TO_WORLD, Conversion.BOX_TO_WORLD, 0f)
    handler.setCombinedMatrix(debugMatrix)
    debugRenderer.render(world, debugMatrix)
    handler.updateAndRender()
    */

    batch.begin()
    def normalProjection:Matrix4 = new Matrix4().setToOrtho2D(0, 0, Gdx.graphics.getWidth(),  Gdx.graphics.getHeight())
    batch.setProjectionMatrix(normalProjection)
    statsFont.draw(batch,room.title,400,170,750,Align.topLeft,true)

    descriptionFont.draw(batch,room.history,30,170,375,Align.topLeft,true)

    menuFont.draw(batch,room.menu,400,140,750,Align.topLeft,true)
    batch.end()

    for (c <- world.getContactList) {
      var t1:Thing = null
      var t2:Thing = null
      if(c.getFixtureA.getUserData != null && c.getFixtureA.getUserData.isInstanceOf[Tile] == false) {
        t1 = c.getFixtureA.getUserData.asInstanceOf[Thing]
      }
      if(c.getFixtureB.getUserData != null && c.getFixtureB.getUserData.isInstanceOf[Tile] == false) {
        t2 = c.getFixtureB.getUserData.asInstanceOf[Thing]
      }

      if(t1 != null && t2 != null) {
        t1.contact(gameTime,t2)
        t2.contact(gameTime,t1)
      }

    }

    for((k,thing) <- room.inventory) {
      thing.cleanup(gameTime)
    }
  }

  def at(r2:Room): Boolean = {
    return room == r2
  }
}