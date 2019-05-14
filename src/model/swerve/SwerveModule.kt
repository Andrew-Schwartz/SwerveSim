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

    val heading: Double
        get() = output.angle
//        get() = field + Swerve.heading
//        private set

    val speed: Double
        get() = output.magnitude
//        private set

    fun set(heading: Double, speed: Double) {
//        this.heading = heading

//        this.speed = speed
//        val dSpeed = speed - this.speed
//        this.speed += speed / 1000.0
//        println(this.speed)

//        val s: Double
//        val h: Double

        val newSpeed = this.speed + (speed - this.speed).bound(0.01)
        output = (-Vector.j * newSpeed)

        if (speed epsilonEquals 0.0) {
//            output *= 0.9
        } else {
            output = output.rotate(heading + Swerve.heading)
        }
        output = output.bound(1.0)
    }

    var output: Vector = Vector.ZERO

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
        DrawableVector(x, y, output * 40.0).draw(applet)
    }

    private companion object {
        const val h = 5F
        const val w = 3 * h
        const val muForward = 0.01
        const val muStrafe = 0.1
    }
}