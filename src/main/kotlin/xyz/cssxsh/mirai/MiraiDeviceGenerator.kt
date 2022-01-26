package xyz.cssxsh.mirai

import kotlinx.serialization.*
import net.mamoe.mirai.*
import net.mamoe.mirai.spi.*
import net.mamoe.mirai.utils.*
import kotlin.random.Random

@OptIn(MiraiExperimentalApi::class)
public class MiraiDeviceGenerator : DeviceInfoService {

    override val priority: Int get() = super.priority - 1

    private var random: Random = Random.Default

    internal var models = listOf(
        Model(
            name = "Redmi Note 9 Pro 5G",
            tac = "864365",
            fac = "05",
            brand = "Xiaomi"
        ),
        Model(
            name = "Redmi Note 8",
            tac = "863971",
            fac = "05",
            brand = "Xiaomi"
        )
    )

    public fun random(random: Random) {
        this.random = random
    }

    public fun models(models: Iterable<Model>) {
        this.models = models.toList()
    }

    @Suppress("INVISIBLE_REFERENCE", "INVISIBLE_MEMBER")
    public override fun load(bot: Bot): DeviceInfo {
        val file = bot.configuration.workingDir.resolve("device.json")
        if (!file.exists() || file.length() == 0L) {
            return generate(bot = bot).also {
                file.writeText(DeviceInfoManager.serialize(it))
            }
        }
        return DeviceInfoManager.deserialize(file.readText())
    }

    public override fun generate(bot: Bot): DeviceInfo {
        val model = models.random(random)
        return DeviceInfo(
            display = "MIRAI.${getRandomIntString(6, random)}.001".toByteArray(),
            product = "mirai".toByteArray(),
            device = "mirai".toByteArray(),
            board = "mirai".toByteArray(),
            brand = model.brand.toByteArray(),
            model = model.name.toByteArray(),
            bootloader = "unknown".toByteArray(),
            fingerprint = "mamoe/mirai/mirai:10/MIRAI.200122.001/${
                getRandomIntString(7, random)
            }:user/release-keys".toByteArray(),
            bootId = generateUUID(getRandomByteArray(16, random).md5()).toByteArray(),
            procVersion = "Linux version 3.0.31-${
                getRandomString(8, random)
            } (android-build@xxx.xxx.xxx.xxx.com)".toByteArray(),
            baseBand = byteArrayOf(),
            version = DeviceInfo.Version(),
            simInfo = "T-Mobile".toByteArray(),
            osType = "android".toByteArray(),
            macAddress = "02:00:00:00:00:00".toByteArray(),
            wifiBSSID = "02:00:00:00:00:00".toByteArray(),
            wifiSSID = "<unknown ssid>".toByteArray(),
            imsiMd5 = getRandomByteArray(16, random).md5(),
            imei = model.imei(),
            apn = "wifi".toByteArray()
        )
    }

    internal fun Model.imei(): String {
        val snr = getRandomIntString(6, random)
        val sp: Int = luhn(tac + fac + snr)
        return tac + fac + snr + sp
    }

    private fun luhn(random: String): Int {
        var odd = false
        val zero = '0'
        return random.sumOf { char ->
            odd = !odd
            if (odd) (char.code - zero.code) else ((char.code - zero.code) * 2).let { it % 10 + it / 10 }
        }.let { (10 - it % 10) % 10 }
    }

    @Serializable
    public data class Model(
        val name: String,
        val brand: String,
        val tac: String,
        val fac: String
    )
}