import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration

object Main {
  def main(args: Array[String]): Unit = {
    println("Main")

    def config = new LwjglApplicationConfiguration()
    new LwjglApplication(new Sinx(), config)
  }
}

