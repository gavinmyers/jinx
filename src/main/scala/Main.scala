import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration

object Main {
  def main(args: Array[String]): Unit = {
    println("Main")

    def config = new LwjglApplicationConfiguration()
    config.width = 960
    config.height = 240
    config.x = 0
    new LwjglApplication(new Sinx())
  }
}

