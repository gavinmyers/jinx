import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration

object Main {
  def main(args: Array[String]): Unit = {
    var config:LwjglApplicationConfiguration = new LwjglApplicationConfiguration();
    config.width = 20 * 48
    config.height = 20 * 48

    //config.fullscreen = true
    println(config.fullscreen)
    new LwjglApplication(new Sinx(), config)
  }
}

