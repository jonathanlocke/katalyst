package jonathanlocke.katalyst.resource

import jonathanlocke.katalyst.logging.LoggerMixin
import jonathanlocke.katalyst.resources.location.ResourceLocation.Companion.toResource

class ResourceTest : LoggerMixin {

    // @Test
    fun testFile() {
        "/tmp/test.txt"
            .toResource(this)
            .streamer()
            .writeText("hello")
    }

    // @Test
    fun testClassPath() {
        "classpath:///jonathanlocke/katalyst/resource/test.txt"
            .toResource(this)
            .streamer()
            .readText()
    }
}