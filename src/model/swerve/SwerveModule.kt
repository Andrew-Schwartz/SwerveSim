package model.swerve

import model.math.DrawableVector
import model.math.Vector
import processing.core.PApplet
import processingExt.Drawable
import processingExt.line

class SwerveModule(var x: Double, var y: Double) : Drawable {
    val center: Vector get() = Vector(x, y)

    var heading: Double = 0.0
        get() = field + Swerve.heading

    var speed: Double = 0.0
        set(speed) {
            output = Vector(0.0, -speed).rotate(heading)
            field = speed
//            val (newX, newY) = Vector(speed, 0.0).rotate(heading - 90) / 1.0
//            println("nx: $newX, ny: $newY")
//            var (x, y) = output
//            if (newX epsilonEquals 0.0) {
//                x *= 0.9
//            } else {
//                x += newX
//            }
//            if (newY epsilonEquals 0.0) {
//                y *= 0.9
//            } else {
//                y += newY
//            }
//            output = Vector(x, y).bound(0.7)
//            field = speed
        }

    var output: Vector = Vector.ZERO

    override fun draw(applet: PApplet) {
        with(applet) {
            noFill()
            stroke(0)
            strokeWeight(2F)
            val points = listOf(
                -w / 2, -h / 2,
                w / 2, -h / 2,
                w / 2, h / 2,
                -w / 2, h / 2
            ).chunked(2) { Vector(it[0], it[1]) }
                .map { center + it }
                .map { it.rotateAround(center, heading) }
            line(points[0], points[1])
            line(points[1], points[2])
            line(points[2], points[3])
            line(points[3], points[0])
        }
        DrawableVector(Vector(x, y), (output * 40.0)).draw(applet)
    }

    private companion object {
        const val w = 15F
        const val h = 3 * w
    }
}