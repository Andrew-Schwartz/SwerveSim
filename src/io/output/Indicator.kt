package io.output

import io.input.Input
import libraryExtensions.*
import processing.core.PApplet

class Indicator(val display: String, val x: Int, val y: Int, val targetKey: NamedKey) : Drawable {
    private val keyIsPressed: Boolean get() = targetKey in Input.pressedKeys

    override fun draw(applet: PApplet) {
        with(applet) {
            stroke(0)
            strokeWeight(1.5F)
            if (keyIsPressed)
                fill(0, 255, 0)
            else
                fill(255, 0, 0)
            arc(x, y, 50, 50, 0, 360)
            noStroke()
            fill(0)
            textSize(30F)
            text(display, x - 7, y + 10)
        }
    }
}