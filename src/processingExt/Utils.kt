package processingExt

import processing.core.PApplet

inline fun <reified T : PApplet> runPApplet() {
    T::class.run {
        PApplet.runSketch(arrayOf(simpleName), objectInstance)
    }
}