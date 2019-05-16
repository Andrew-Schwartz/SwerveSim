package model.swerve

import model.math.DrawableVector
import model.math.Vector
import model.math.Vector.Companion.jHat
import model.math.bound
import processing.core.PApplet
import processingExt.Drawable
import processingExt.line
import kotlin.math.abs

class SwerveModule(var x: Double, var y: Double) : Drawable {
    var center: Vector
        get() = Vector(x, y)
        set(value) {
            x = value.x
            y = value.y
        }

    var outputForce: Vector = Vector.ZERO

    var targetHeading: Double = 0.0
    var currentHeading: Double = 0.0

    var accelerating: Boolean = false

    val heading: Double
        get() = if (!accelerating)
            oldHeading
        else
            outputForce.angle + 90

    var oldHeading: Double = heading

    val speed: Double
        get() = outputForce.magnitude

    fun set(heading: Double, speed: Double, moving: Boolean) {
        this.accelerating = moving
        targetHeading = if (!moving) {
            oldHeading
        } else {
            oldHeading = this.heading
            heading + Swerve.heading
        }

        var newSpeed = this.speed + (speed - this.speed).bound(0.05) // max acceleration of wheel

        fun difference() = currentHeading - targetHeading

        do {
            if (difference() > 180)
                targetHeading += 360
            if (difference() < -180)
                targetHeading -= 360
        } while (abs(difference()) > 180)

        if (abs(difference()) > 90) {
            if (difference() > 90)
                targetHeading += 180
            if (difference() < -90)
                targetHeading -= 180
            newSpeed *= -1
        }

        currentHeading += (targetHeading - currentHeading) / 5.0


        outputForce = (-jHat * newSpeed)
            .rotate(currentHeading)
//            .bound(maxOutput)
    }

    fun frictionForce(velocity: Vector): Vector {
        // zeroed relative to this output vel
        val thisVel = outputForce.rotate(-heading + 90)
        val inVel = velocity.rotate(-heading + 90)

        return Vector(inVel.x - thisVel.x, inVel.y).rotate(heading + 90) * μ
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
            DrawableVector(x, y, outputForce * 40.0 / maxOutput).draw(this)
        }
    }

    @Suppress("NonAsciiCharacters", "ObjectPropertyName")
    companion object {
        private const val w = 4F // cm, ~= 1.5 in
        private const val h = 15F // cm, ~= 6 in

        private const val μ = 1.0

        const val maxOutput = 3.0
    }
}