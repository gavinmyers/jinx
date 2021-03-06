import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration

object Main {
  def main(args: Array[String]): Unit = {

    val config:LwjglApplicationConfiguration = new LwjglApplicationConfiguration()
    config.width = 24 * 48
    config.height = 12 * 48

    config.fullscreen = false
    //println(config.fullscreen)
    new LwjglApplication(new Sinx(), config)

  }
}

