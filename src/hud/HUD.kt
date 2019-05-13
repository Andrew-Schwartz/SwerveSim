package hud

import Sketch.h
import model.swerve.Swerve
import processing.core.PApplet
import processingExt.Drawable
import processingExt.NamedKey.*
import processingExt.rect

object HUD : Drawable {
    const val dividerX = 400
    val indicators: Array<Indicator> = arrayOf(
        Indicator("↑", dividerX / 2, 50, UP_ARROW),
        Indicator("↓", dividerX / 2, 190, DOWN_ARROW),
        Indicator("←", dividerX / 2 - 70, 120, LEFT_ARROW),
        Indicator("→", dividerX / 2 + 70, 120, RIGHT_ARROW),
        Indicator("Z", dividerX / 2 - 70, 50, Z),
        Indicator("X", dividerX / 2 + 70, 50, X)
    )

    override fun draw(applet: PApplet) {
        with(applet) {
            fill(0)
            noStroke()
            rect(dividerX - 2, 0, 2, h)
            indicators.forEach { it.draw(this) }

            text("X =", 50F, 300F)
            text(Swerve.x.toFloat(), 105F, 300F)
            text("Y =", 50F, 350F)
            text(-Swerve.y.toFloat(), 105F, 350F)
            text("θ = ", 50F, 400F)
            text(Swerve.heading.toFloat(), 105F, 400F)

            text("dX =", 50F, 475F)
            text(Swerve.linearMomentum.x.toFloat(), 125F, 475F)
            text("dY =", 50F, 525F)
            text(-Swerve.linearMomentum.y.toFloat(), 125F, 525F)
            text("dθ = ", 50F, 575F)
            text(Swerve.angularMomentum.toFloat(), 125F, 575F)
        }
    }
}