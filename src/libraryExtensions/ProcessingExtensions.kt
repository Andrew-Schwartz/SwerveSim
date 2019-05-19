package libraryExtensions

import model.math.Vector
import processing.core.PApplet
import kotlin.math.max

fun PApplet.line(x1: Number, y1: Number, x2: Number, y2: Number) {
    line(x1.toFloat(), y1.toFloat(), x2.toFloat(), y2.toFloat())
}

fun PApplet.line(p1: Vector, p2: Vector) {
    val (x1, y1) = p1
    val (x2, y2) = p2
    line(x1, y1, x2, y2)
}

fun PApplet.rect(x1: Number, y1: Number, x2: Number, y2: Number) {
    rect(x1.toFloat(), y1.toFloat(), x2.toFloat(), y2.toFloat())
}

fun PApplet.arc(x: Number, y: Number, w: Number, h: Number, start: Number, end: Number) {
    arc(x.toFloat(), y.toFloat(), w.toFloat(), h.toFloat(), start.toFloat(), end.toFloat())
}

fun PApplet.fill(r: Number, g: Number, b: Number) {
    fill(r.toFloat(), g.toFloat(), b.toFloat())
}

fun PApplet.text(s: String, x: Number, y: Number) {
    text(s, x.toFloat(), y.toFloat())
}

fun PApplet.vector(x: Double, y: Double, v: Vector) {
    val center = Vector(x, y)
    with(this) {
        val (_, _, mag, angle) = v
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