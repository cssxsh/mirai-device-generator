package xyz.cssxsh.mirai.plugin

import kotlinx.coroutines.*
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import net.mamoe.mirai.Bot
import net.mamoe.mirai.console.*
import net.mamoe.mirai.console.extension.*
import net.mamoe.mirai.console.plugin.jvm.*
import net.mamoe.mirai.spi.*
import net.mamoe.mirai.utils.*
import xyz.cssxsh.mirai.*

public object MiraiDevicePlugin : KotlinPlugin(
    JvmPluginDescription(
        id = "xyz.cssxsh.mirai.mirai-device-generator",
        name = "mirai-device-generator",
        version = "1.0.0-dev",
    ) {
        author("cssxsh")
    }
) {
    private val generator = MiraiDeviceGenerator()

    @OptIn(MiraiExperimentalApi::class)
    override fun PluginComponentStorage.onLoad() {
        DeviceInfoService.setService(generator)
    }

    @OptIn(MiraiExperimentalApi::class, ExperimentalSerializationApi::class)
    override fun onEnable() {
        val json = Json { prettyPrint = true }

        with(dataFolder.resolve("models.json")) {
            if (exists()) {
                generator.models = json.decodeFromString(readText())
            } else {
                writeText(json.encodeToString(generator.models))
            }
        }

        with(dataFolder.resolve("sdks.json")) {
            if (exists()) {
                generator.sdks = json.decodeFromString(readText())
            } else {
                writeText(json.encodeToString(generator.sdks))
            }
        }

        launch {
            while (isActive) {
                val device = MiraiConsole.rootDir.resolve("device.json")
                if (device.exists().not()) {
                    @Suppress("INVISIBLE_REFERENCE", "INVISIBLE_MEMBER")
                    device.writeText(DeviceInfoManager.serialize(generator.generate()))
                }
                delay(10_000)
            }
        }

        launch {
            while (isActive) {
                Bot.instances.forEach { bot ->
                    val device = bot.configuration.workingDir.resolve("device.json")
                    if (device.exists().not()) {
                        @Suppress("INVISIBLE_REFERENCE", "INVISIBLE_MEMBER")
                        device.writeText(DeviceInfoManager.serialize(generator.generate()))
                    }
                }
                delay(10_000)
            }
        }
    }
}