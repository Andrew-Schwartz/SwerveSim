package io.input

import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class MayExist<T>(
    private val default: T,
    private val getter: () -> T
) : ReadOnlyProperty<Controller, T> {
    override fun getValue(thisRef: Controller, property: KProperty<*>): T =
        if (thisRef.exists) getter()
        else default
}

class Button(
    private val index: Int
) : ReadOnlyProperty<Controller, Boolean> {
    override fun getValue(thisRef: Controller, property: KProperty<*>): Boolean =
        if (thisRef.exists) thisRef.buttonsMap and (1 shl index) != 0
        else false
}