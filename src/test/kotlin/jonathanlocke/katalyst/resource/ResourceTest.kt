package jonathanlocke.katalyst.resource

import jonathanlocke.katalyst.logging.LoggerMixin
import jonathanlocke.katalyst.resources.location.ResourceLocation.Companion.toResource
import kotlin.test.Test

class ResourceTest : LoggerMixin {

    @Test
    fun test() {
        "/tmp/test.txt"
            .toResource(this)
            .streamer()
            .writeText("hello")
    }
}