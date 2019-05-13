package model.swerve

import Keys
import model.math.DrawableVector
import model.math.Vector
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
    var angularSpeed = 0.0

    val momentumIndicator: DrawableVector get() = linearMomentum.times(20.0).drawable(x, y)

    val moduleOffsets: List<Vector>
        get() = listOf(
            -s / 2, -s / 2,
            s / 2, -s / 2,
            s / 2, s / 2,
            -s / 2, s / 2
        ).chunked(2, ::Vector)
            .map { it.rotate(heading) }

    val modules = moduleOffsets
        .map { SwerveModule(it.x, it.y) }
        .map {
            it.x += x
            it.y += y
            it
        }

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
//        var moving = false
//
//        if (Keys.rotationSpeed != 0) {
//            for ((i, module) in modules.withIndex()) {
//                module.heading = (module.center - center).angle + 90
//                module.speed = Keys.rotationSpeed.toDouble()
//            }
//            moving = true
//        }
//
//        if (Keys.direction != Vector(0, 0)) {
//            for (it in modules) {
//                it.heading = Keys.direction.angle
//                it.speed = Keys.direction.magnitude
//            }
//            moving = true
//        }
//
//        if (!moving)
//            for (it in modules)
//                it.speed = 0.0

        val (strafe, forward) = Keys.direction.rotate(heading)

        val a = strafe - Keys.rotationSpeed * sComponent
        val b = strafe + Keys.rotationSpeed * sComponent
        val c = forward - Keys.rotationSpeed * sComponent
        val d = forward + Keys.rotationSpeed * sComponent

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

        for ((i, module) in modules.withIndex()) {
            module.heading = wa[i]
            module.speed = ws[i]
        }
    }

    fun move() {
        linearMomentum = modules.map { it.output }.reduce { acc, vector -> acc + vector }
        angularSpeed = modules.map {
            val e1 = Vector(x, y) - it.center
            val e2 = it.center - (it.center + it.output)
            val clockWise = e1.x * e2.y - e1.y * e2.x >= 0
            if (clockWise) it.output.magnitude else -it.output.magnitude
        }.sum()

        x += linearMomentum.x
        y += linearMomentum.y
        heading += angularSpeed
    }

    private const val s = 150.0
    private val sComponent = s / hypot(s, s)
}