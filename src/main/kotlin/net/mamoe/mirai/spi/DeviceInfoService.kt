package net.mamoe.mirai.spi

import net.mamoe.mirai.Bot
import net.mamoe.mirai.utils.*

/**
 * 生成设备信息
 *
 * @author cssxsh
 */
@MiraiExperimentalApi
@Suppress("INVISIBLE_REFERENCE", "INVISIBLE_MEMBER")
public interface DeviceInfoService : BaseService {

    public fun load(bot: Bot): DeviceInfo

    public fun generate(): DeviceInfo

    public object Default : DeviceInfoService {
        override fun load(bot: Bot): DeviceInfo {
            val file = bot.configuration.workingDir.resolve("device.json")
            if (!file.exists() || file.length() == 0L) {
                return generate().also {
                    file.writeText(DeviceInfoManager.serialize(it))
                }
            }
            return DeviceInfoManager.deserialize(file.readText())
        }

        override fun generate(): DeviceInfo {
            return DeviceInfo.random()
        }
    }

    public companion object INSTANCE : DeviceInfoService {
        private val loader = SPIServiceLoader(Default, DeviceInfoService::class.java)

        override fun load(bot: Bot): DeviceInfo {
            return loader.service.load(bot)
        }

        override fun generate(): DeviceInfo {
            return loader.service.generate()
        }

        @JvmStatic
        public fun setService(service: DeviceInfoService) {
            loader.service = service
        }
    }
}