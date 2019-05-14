import hud.HUD
import model.swerve.Swerve
import processing.core.PApplet
import processing.event.KeyEvent
import processingExt.NamedKey.Companion.named

object Sketch : PApplet() {
    const val w = 1600
    const val h = 800

    override fun settings() {
        size(w, h)
    }

    override fun draw() {
        background(255)
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