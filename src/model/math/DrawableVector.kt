package model.math

import processing.core.PApplet
import processingExt.Drawable
import processingExt.line
import kotlin.math.max

open class DrawableVector(val center: Vector, val dir: Vector) : Drawable {
    constructor(x: Double, y: Double, dir: Vector) : this(Vector(x, y), dir)

    override fun draw(applet: PApplet) {
        with(applet) {
            val (_, _, mag, angle) = dir
            val endPoint = center + Vector(mag, 0)
            val firstPoint = (endPoint - Vector(mag / 4, 0)).rotateAround(center, 10.0)
            val secondPoint = (endPoint - Vector(mag / 4, 0)).rotateAround(center, -10.0)
            stroke(0F, 0F, 255F)
            strokeWeight(max((Math.cbrt(mag) - 1).toFloat(), 0F))
            val points = arrayOf(endPoint, firstPoint, secondPoint)
                .map { it.rotateAround(center, angle) }
            line(center, points[0])
            line(points[1], points[0])
            line(points[2], points[0])
        }
    }
}