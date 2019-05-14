package model.swerve

import model.math.DrawableVector
import model.math.Vector
import model.math.bound
import model.math.epsilonEquals
import processing.core.PApplet
import processingExt.Drawable
import processingExt.line
import java.lang.Math.toRadians
import kotlin.math.absoluteValue
import kotlin.math.cos
import kotlin.math.sin

class SwerveModule(var x: Double, var y: Double) : Drawable {
    var center: Vector
        get() = Vector(x, y)
        set(value) {
            x = value.x
            y = value.y
        }

    var output: Vector = Vector.ZERO

    val heading: Double
        get() = output.angle + 90

    var oldHeading: Double = heading

    val speed: Double
        get() = output.magnitude

    fun set(heading: Double, speed: Double) {
//        if (!(this.speed epsilonEquals 0.0)) oldHeading = this.heading

        val h = if (this.speed epsilonEquals 0.0) {
            oldHeading
        } else {
            oldHeading = this.heading
            if (speed epsilonEquals 0.0)
                this.heading
            else
                heading + Swerve.heading
        }

        val newSpeed = this.speed + (speed - this.speed).bound(0.05)

        output = (-Vector.j * newSpeed)
            .rotate(h)
            .bound(1.0)

        println("old: $oldHeading, h: $h")
    }

    fun frictionForce(heading: Double): Double {
        val forwardFriction = muForward * sin(toRadians(this.heading - heading)).absoluteValue
        val strafeFriction = muStrafe * cos(toRadians(this.heading - heading)).absoluteValue
        return forwardFriction + strafeFriction
    }

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
            ).chunked(2) { Vector(it) }
                .map { center + it }
                .map { it.rotateAround(center, heading) }
            line(points[0], points[1])
            line(points[1], points[2])
            line(points[2], points[3])
            line(points[3], points[0])
        }
        DrawableVector(x, y, Vector.i.rotate(output.angle) * 40.0).draw(applet)
    }

    private companion object {
        const val w = 5F
        const val h = 3 * w
        const val muForward = 0.01
        const val muStrafe = 0.1
    }
}