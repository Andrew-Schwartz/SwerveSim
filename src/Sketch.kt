import hud.HUD
import model.swerve.Swerve
import processing.core.PApplet
import processing.event.KeyEvent
import processingExt.NamedKey.Companion.named

object Sketch : PApplet() {
    const val w = 1600.0
    const val h = 800.0

    override fun settings() {
        size(w.toInt(), h.toInt())
    }

    override fun draw() {
//        frameRate(10F)

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