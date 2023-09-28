package xyz.cssxsh.mirai.device

import kotlinx.coroutines.*
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import net.mamoe.mirai.console.extension.*
import net.mamoe.mirai.console.plugin.jvm.*
import net.mamoe.mirai.event.*

@PublishedApi
internal object MiraiDevicePlugin : KotlinPlugin(
    JvmPluginDescription(
        id = "xyz.cssxsh.mirai.mirai-device-generator",
        name = "mirai-device-generator",
        version = "1.3.0",
    ) {
        author("cssxsh")
    }
) {
    private val generator = MiraiDeviceGenerator()

    override fun PluginComponentStorage.onLoad() {
        contributeBotConfigurationAlterer { _, configuration ->
            configuration.deviceInfo = generator::load
            configuration
        }
    }

    override fun onEnable() {
        with(dataFolder.resolve("models.json")) {
            if (exists()) {
                generator.models = Json.decodeFromString(readText())
            }
        }
        with(dataFolder.resolve("sdks.json")) {
            if (exists()) {
                generator.sdks = Json.decodeFromString(readText())
            }
        }
        with(dataFolder.resolve("mac.json")) {
            if (exists()) {
                generator.addr = Json.decodeFromString(readText())
            }
        }
        MiraiDeviceReset.registerTo(globalEventChannel())
    }

    override fun onDisable() {
        MiraiDeviceReset.cancel()
    }
}