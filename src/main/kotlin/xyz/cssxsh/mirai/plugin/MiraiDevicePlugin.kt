package xyz.cssxsh.mirai.plugin

import net.mamoe.mirai.console.plugin.jvm.*
import net.mamoe.mirai.utils.*

public object MiraiDevicePlugin : KotlinPlugin(
    JvmPluginDescription(
        id = "xyz.cssxsh.mirai.mirai-device-generator",
        name = "mirai-device-generator",
        version = "1.0.0-dev",
    ) {
        author("cssxsh")
    }
) {
    override fun onEnable() {
        logger.info { "Plugin loaded" }
    }
}