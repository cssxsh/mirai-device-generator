package xyz.cssxsh.mirai.device

import net.mamoe.mirai.console.events.*
import net.mamoe.mirai.event.*
import net.mamoe.mirai.network.*
import net.mamoe.mirai.utils.*

/**
 * 自动重载 Device Info
 */
public object MiraiDeviceReset : SimpleListenerHost() {

    @EventHandler
    public fun ConsoleEvent.handle() {
        try {
            val failure = this as? AutoLoginEvent.Failure ?: return
            val exception = failure.cause.suppressedExceptions
                .firstNotNullOfOrNull { it as? WrongPasswordException } ?: return
            val message = exception.message ?: return
            if ("""code=(45|235)""".toRegex() !in message) return
            val success = with(failure.bot.configuration.workingDir) {
                val file = resolve("device.json")
                val backup = resolve("device.${System.currentTimeMillis()}.json")
                file.renameTo(backup)
            }
            if (success) {
                bot.logger.info { "device.json 可能已经被拉黑, 将重新生成，请稍后重新尝试手动重新登陆" }
            }
        } catch (_: ClassNotFoundException) {
            // ...
        }
    }
}