import io.Controller
import io.HUD
import io.Input
import model.swerve.Swerve
import org.lwjgl.glfw.GLFW.GLFW_JOYSTICK_1
import org.lwjgl.glfw.GLFW.glfwInit
import processing.core.PApplet
import processing.event.KeyEvent
import processingExt.NamedKey.Companion.named

object Sketch : PApplet() {
    const val w = 1600.0
    const val h = 800.0

    override fun settings() {
        if (!glfwInit())
            throw IllegalStateException("Unable to initialize GLFW")

        size(w.toInt(), h.toInt())
        Input.controllers += Controller(GLFW_JOYSTICK_1)
    }

    override fun draw() {
//        frameRate(10F)
        background(255)
        Input.readControllers()
        HUD.draw(this)
        Swerve.updateModules()
        Swerve.move()
        Swerve.draw(this)
    }

    override fun keyPressed(event: KeyEvent) {
        Input += event.named()
    }

    override fun keyReleased(event: KeyEvent) {
        Input -= event.named()
    }
}