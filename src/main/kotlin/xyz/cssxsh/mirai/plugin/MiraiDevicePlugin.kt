package xyz.cssxsh.mirai.plugin

import kotlinx.serialization.*
import kotlinx.serialization.json.*
import net.mamoe.mirai.console.extension.*
import net.mamoe.mirai.console.plugin.jvm.*
import xyz.cssxsh.mirai.*

public object MiraiDevicePlugin : KotlinPlugin(
    JvmPluginDescription(
        id = "xyz.cssxsh.mirai.mirai-device-generator",
        name = "mirai-device-generator",
        version = "1.0.0",
    ) {
        author("cssxsh")
    }
) {
    private val generator = MiraiDeviceGenerator()
    private val json = Json { prettyPrint = true }

    override fun PluginComponentStorage.onLoad() {
        contributeBotConfigurationAlterer { _, configuration ->
            configuration.deviceInfo = generator::load
            configuration
        }
    }

    @OptIn(ExperimentalSerializationApi::class)
    override fun onEnable() {
        with(dataFolder.resolve("models.json")) {
            if (exists().not()) {
                writeText(getResource("xyz/cssxsh/mirai/plugin/models.json") ?: throw NoSuchElementException("models.json"))
            }
            generator.models = json.decodeFromString(readText())
        }
        with(dataFolder.resolve("sdks.json")) {
            if (exists().not()) {
                writeText(getResource("xyz/cssxsh/mirai/plugin/sdks.json") ?: throw NoSuchElementException("sdks.json"))
            }
            generator.sdks = json.decodeFromString(readText())
        }
        with(dataFolder.resolve("mac.json")) {
            if (exists().not()) {
                writeText(getResource("xyz/cssxsh/mirai/plugin/mac.json") ?: throw NoSuchElementException("mac.json"))
            }
            generator.addr = json.decodeFromString(readText())
        }
    }
}