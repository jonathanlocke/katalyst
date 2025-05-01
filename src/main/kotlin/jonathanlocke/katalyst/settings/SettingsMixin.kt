package jonathanlocke.katalyst.settings

import jonathanlocke.katalyst.mixins.Mixin
import jonathanlocke.katalyst.reflection.ValueType

interface SettingsMixin : Mixin {

    fun <Settings : Any> settings(type: ValueType<Settings>, settings: Settings): Settings {
        return mixinValue(type) { settings }
    }
}
