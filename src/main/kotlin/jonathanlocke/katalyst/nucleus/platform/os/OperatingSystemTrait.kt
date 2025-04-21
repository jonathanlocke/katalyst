package jonathanlocke.katalyst.nucleus.platform.os

import jonathanlocke.katalyst.nucleus.platform.os.OperatingSystem.Type.UNIX
import jonathanlocke.katalyst.nucleus.platform.os.OperatingSystem.Type.WINDOWS

interface OperatingSystemTrait {

    fun os(): OperatingSystem = OperatingSystem.get()
    fun env(): Set<EnvironmentVariable> = os().environmentVariables()
    fun env(name: String) = EnvironmentVariable(name)
    fun isUnix(): Boolean = os().type == UNIX
    fun isWindows(): Boolean = os().type == WINDOWS
}
