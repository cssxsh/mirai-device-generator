package xyz.cssxsh.mirai.device

import kotlinx.serialization.builtins.*
import kotlinx.serialization.json.*
import net.mamoe.mirai.console.plugin.ResourceContainer.Companion.asResourceContainer
import org.junit.jupiter.api.*

internal class MiraiDeviceGeneratorTest {

    private val container = MiraiDeviceGenerator::class.asResourceContainer()

    @Test
    fun model() {
        Json.decodeFromString(
            ListSerializer(MiraiDeviceGenerator.Model.serializer()),
            container.getResource("models.json")!!
        )
        Json.decodeFromString(
            ListSerializer(MiraiDeviceGenerator.SdkVersion.serializer()),
            container.getResource("sdks.json")!!
        )
        Json.decodeFromString(
            MapSerializer(String.serializer(), ListSerializer(String.serializer())),
            container.getResource("mac.json")!!
        )
    }
}