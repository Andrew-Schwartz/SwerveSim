package model.swerve

import Input
import Sketch
import hud.HUD
import model.math.Vector
import model.math.drawable
import model.math.epsilonEquals
import model.math.sum
import model.swerve.SwerveModule.Companion.maxOutput
import processing.core.PApplet
import processingExt.Drawable
import processingExt.line
import kotlin.math.*

object Swerve : Drawable {
    var x = 1000.0
    var y = 400.0
    var center
        get() = Vector(x, y)
        set(value) {
            x = value.x
            y = value.y
        }

    var fieldCentric = true

    var heading: Double = 0.0

    var linearMomentum = Vector(0, 0)
    var angularMomentum = 0.0

    private val moduleOffsets: List<Vector>
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
        linearMomentum.times(20.0 / maxOutput).drawable(center).draw(applet)
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
        val (strafe, forward) = if (fieldCentric)
            Input.direction.rotate(heading)
        else
            Input.direction

        // geometric stuff
        val a = strafe - Input.rotationSpeed * lComponent
        val b = strafe + Input.rotationSpeed * lComponent
        val c = forward - Input.rotationSpeed * wComponent
        val d = forward + Input.rotationSpeed * wComponent

        // wheel speed
        var ws = listOf(
            hypot(b, d),
            hypot(b, c),
            hypot(a, c),
            hypot(a, d)
        ).map { it * maxOutput }

        // wheel angle
        val wa = listOf(
            atan2(b, d) * 180 / PI,
            atan2(b, c) * 180 / PI,
            atan2(a, c) * 180 / PI,
            atan2(a, d) * 180 / PI
        )

        val maxSpeed = ws.max()!!
        if (maxSpeed > maxOutput)
            ws = ws.map { it * maxOutput / maxSpeed }

        val moving = !ws.all { it epsilonEquals 0.0 }

//        println("wa: $wa\t\tws: $ws")

        for ((i, module) in modules.withIndex()) {
            if (moving) {
                module.set(wa[i], ws[i])
            } else {
                module.set(module.heading - heading, ws[i])
            }
        }
    }

    fun move() {
        val linearForce = modules.map { it.output }.sum()
        val angularForce = modules.map {
            it.output/*.bound(0.5)*/ dot Vector(1.0, 0.0).rotate((it.center - center).angle + 90)
        }.sum()

        linearMomentum = linearForce
        angularMomentum = angularForce

//        linearMomentum = linearForce.map(linearMomentum) { f, m -> m + f / 10.0 }
        linearMomentum *= 1.0 - modules.map { it.frictionForce(linearMomentum.angle) }.average()
//        linearMomentum = linearMomentum

//        angularMomentum = if (angularForce epsilonEquals 0.0)
//            angularMomentum * 0.9
//        else
//            angularMomentum + angularForce / 10.0
//        angularMomentum *= 1.0 - modules.map { it.frictionForce((it.center - center).angle + 90) }.average()

        x += linearMomentum.x
        y += linearMomentum.y
        heading += angularMomentum

        x = max(HUD.dividerX.toDouble(), min(Sketch.w.toDouble(), x))
        y = max(0.0, min(Sketch.h.toDouble(), y))
    }

    private const val w = 75.0
    private const val l = 75.0
    private val wComponent = w / hypot(w, l)
    private val lComponent = l / hypot(w, l)
}