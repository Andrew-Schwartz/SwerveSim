package libraryExtensions

import processing.event.KeyEvent

enum class NamedKey(val key: Int) {
    TAB(9),
    ENTER(10),
    SHIFT(16),
    CONTROL(17),
    ALT(18),
    CAPS(20),
    SPACE(32),
    LEFT_ARROW(37),
    UP_ARROW(38),
    RIGHT_ARROW(39),
    DOWN_ARROW(40),
    COMMA(44),
    PERIOD(46),
    FORWARD_SLASH(47),
    WINDOWS(524),
    A(65),
    C(67),
    D(68),
    G(71),
    X(88),
    Z(90);

    companion object {
        fun KeyEvent.named(): NamedKey = try {
            values().first { it.key == this.keyCode }
        } catch (e: NoSuchElementException) {
            println(keyCode)
            ENTER
        }
    }
}
