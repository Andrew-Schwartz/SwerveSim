import io.input.Input
import io.input.LogitechController
import io.output.HUD
import libraryExtensions.NamedKey.Companion.named
import model.swerve.Swerve
import org.lwjgl.glfw.GLFW.GLFW_JOYSTICK_1
import org.lwjgl.glfw.GLFW.glfwInit
import processing.core.PApplet
import processing.event.KeyEvent

object Sketch : PApplet() {
    const val w = 1600.0
    const val h = 800.0

    override fun settings() {
        if (!glfwInit())
            throw IllegalStateException("Unable to initialize GLFW")

        size(w.toInt(), h.toInt())
        Input.controller = LogitechController(GLFW_JOYSTICK_1)
    }

    override fun draw() {
//        frameRate(10F)
        background(255)
        Input.readControllers()
        HUD.draw(this)
        Swerve.calculateModules()
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