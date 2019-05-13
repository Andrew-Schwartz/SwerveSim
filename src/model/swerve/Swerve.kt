package model.swerve

import Keys
import model.math.*
import processing.core.PApplet
import processingExt.Drawable
import processingExt.line
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.hypot

object Swerve : Drawable {
    var x = 1000.0
    var y = 400.0
    val center get() = Vector(x, y)

    var heading: Double = 0.0

    var linearMomentum = Vector(0, 0)
    var angularMomentum = 0.0

    val momentumIndicator: DrawableVector get() = linearMomentum.times(20.0).drawable(x, y)

    val moduleOffsets: List<Vector>
        get() = listOf(
            -w / 2, -l / 2,
            w / 2, -l / 2,
            w / 2, l / 2,
            -w / 2, l / 2
        ).chunked(2, ::Vector)
            .map { it.rotate(heading) }

    val modules = moduleOffsets
        .map { it + center }
        .map { SwerveModule(it.x, it.y) }

    override fun draw(applet: PApplet) {
        for ((i, module) in modules.withIndex()) {
            module.x = x + moduleOffsets[i].x
            module.y = y + moduleOffsets[i].y
            module.draw(applet)
        }
        momentumIndicator.draw(applet)
        with(applet) {
            noFill()
            stroke(0)
            strokeWeight(2F)
            line(modules[0].center, modules[1].center)
            line(modules[1].center, modules[2].center)
            line(modules[2].center, modules[3].center)
            line(modules[3].center, modules[0].center)
        }
    }

    fun updateModules() {
        val (strafe, forward) = Keys.direction.rotate(heading)

        val a = strafe - Keys.rotationSpeed * lComponent
        val b = strafe + Keys.rotationSpeed * lComponent
        val c = forward - Keys.rotationSpeed * wComponent
        val d = forward + Keys.rotationSpeed * wComponent

        // wheel speed
        var ws = listOf(
            hypot(b, d),
            hypot(b, c),
            hypot(a, c),
            hypot(a, d)
        )

        // wheel azimuth
        val wa = listOf(
            atan2(b, d) * 180 / PI,
            atan2(b, c) * 180 / PI,
            atan2(a, c) * 180 / PI,
            atan2(a, d) * 180 / PI
        )

        val maxSpeed = ws.max()!!
        if (maxSpeed > 1.0)
            ws = ws.map { it / maxSpeed }

        val moving = !wa.all { it epsilonEquals 0.0 }

        for ((i, module) in modules.withIndex()) {
            if (moving) module.heading = wa[i]
            module.speed = ws[i]
        }
    }

    fun move() {
        val linearForce = modules.map { it.output }.reduce { acc, vector -> acc + vector }
        val angularForce = modules.map {
            val e1 = Vector(x, y) - it.center
            val e2 = it.center - (it.center + it.output)
            val clockWise = e1.x * e2.y - e1.y * e2.x >= 0
            if (clockWise) it.output.magnitude else -it.output.magnitude
        }.sum()

        linearMomentum = linearForce.map(linearMomentum) { f, m ->
            if (f epsilonEquals 0.0)
                m * 0.9
            else
                m + f / 10.0
        }
        linearMomentum = linearMomentum.bound(8.0)

        angularMomentum = if (angularForce epsilonEquals 0.0)
            angularMomentum * 0.9
        else
            angularMomentum + angularForce / 10.0
        angularMomentum = angularMomentum.bound(4.0)

        x += linearMomentum.x
        y += linearMomentum.y
        heading += angularMomentum
    }

    private const val w = 150.0
    private const val l = 160.0
    private val wComponent = w / hypot(w, l)
    private val lComponent = l / hypot(w, l)
}