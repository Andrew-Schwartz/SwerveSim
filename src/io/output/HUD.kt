package io.output

import Sketch.h
import io.input.Input
import libraryExtensions.Drawable
import libraryExtensions.NamedKey.*
import libraryExtensions.rect
import libraryExtensions.vector
import model.math.mapY
import model.math.round
import model.swerve.Swerve
import model.swerve.dt
import processing.core.PApplet

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
            textSize(30F)
            fill(0)
            noStroke()
            rect(dividerX - 2, 0, 2, h)
            indicators.forEach { it.draw(this) }
            vector(dividerX / 2.0, 120.0, Input.direction.mapY { -it } * 30.0)

            text("X = ${Swerve.x.round(2)}", 50F, 300F)
            text("Y = ${-Swerve.y.round(2)}", 50F, 350F)
            text("θ = ${(Swerve.heading % 360).round(2)}", 50F, 400F)

            text("dX = ${(Swerve.linearVelocity.x / dt).round(2)}", 50F, 475F)
            text("dY = ${(-Swerve.linearVelocity.y / dt).round(2)}", 50F, 525F)
            text("dθ = ${(Swerve.angularVelocity / dt).round(2)}", 50F, 575F)

            textSize(15F)
            text("fps = ${frameRate.toInt()}", 50F, 750F)
        }
    }
}