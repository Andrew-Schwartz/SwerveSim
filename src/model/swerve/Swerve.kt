package model.swerve

import Input
import Sketch
import hud.HUD
import model.math.*
import model.math.Vector.Companion.iHat
import model.swerve.SwerveModule.Companion.maxOutput
import processing.core.PApplet
import processingExt.Drawable
import processingExt.line
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.hypot

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

    var linearVelocity = Vector(0, 0)
    var angularVelocity = 0.0

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
        linearVelocity.times(20.0 / maxOutput).drawable(center).draw(applet)
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

        for ((i, module) in modules.withIndex())
            module.set(wa[i], ws[i], moving)

//        println("wa: $wa\t\tws: $ws")
    }

    fun move() {
        val linearAcceleration = modules.map { it.outputForce }.sum()/* / mass*/
        val angularForce = modules.map {
            it.outputForce dot iHat.rotate((it.center - center).angle + 90)
        }.sum()/* / mass*/

        linearVelocity += linearAcceleration * dt
        linearVelocity += modules.map { it.frictionForce(linearVelocity / 4.0) }.sum()

        angularVelocity += angularForce * dt
        angularVelocity *= 0.97 // TODO actual friction
//        angularVelocity += modules.map { it.frictionForce(-jHat.rotate((it.center - center).angle)) }.sum().magnitude

        center += linearVelocity
        heading += angularVelocity

        x = x.bound(HUD.dividerX, Sketch.w)
        y = y.bound(0.0, Sketch.h)
    }

    private const val w = 76.0 // cm, ~= 30 in
    private const val l = 76.0
    const val mass = 54.4 // kg, ~= 120 lbs

    private val wComponent = w / hypot(w, l)
    private val lComponent = l / hypot(w, l)
}
