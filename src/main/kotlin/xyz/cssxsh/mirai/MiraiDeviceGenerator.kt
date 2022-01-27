package xyz.cssxsh.mirai

import kotlinx.serialization.*
import net.mamoe.mirai.*
import net.mamoe.mirai.spi.*
import net.mamoe.mirai.utils.*
import kotlin.random.*

@OptIn(MiraiExperimentalApi::class)
@Suppress("unused")
public class MiraiDeviceGenerator : DeviceInfoService {

    override val priority: Int get() = super.priority - 1

    internal var random: Random = Random.Default

    internal var models = listOf(
        Model(
            name = "Redmi Note 9 Pro 5G",
            tac = "864365",
            fac = "05",
            board = "sm7225",
            brand = "Xiaomi",
            device = "sagit",
            display = "OPR1.170623.027"
        ),
        Model(
            name = "Redmi Note 8",
            tac = "863971",
            fac = "05",
            board = "msm8953",
            brand = "Xiaomi",
            device = "sagit",
            display = "OPR1.170623.027"
        )
    )

    internal var sdks = listOf(
        SdkVersion(
            incremental = "5891938",
            release = "10",
            codename = "REL",
            sdk = 29
        )
    )

    internal var addr = mapOf(
        "Xiaomi" to listOf("c4:6a:b7")
    )

    @Suppress("INVISIBLE_REFERENCE", "INVISIBLE_MEMBER")
    public override fun load(bot: Bot): DeviceInfo {
        val file = bot.configuration.workingDir.resolve("device.json")
        if (!file.exists() || file.length() == 0L) {
            return generate().also {
                file.writeText(DeviceInfoManager.serialize(it))
            }
        }
        return DeviceInfoManager.deserialize(file.readText())
    }

    public override fun generate(): DeviceInfo {
        val model = models.random(random)
        val sdk = model.sdks.randomOrNull(random) ?: sdks.random(random)
        return DeviceInfo(
            display = model.display.toByteArray(),
            product = model.name.toByteArray(),
            device = model.device.toByteArray(),
            board = model.board.toByteArray(),
            brand = model.brand.toByteArray(),
            model = model.model.ifBlank { model.name }.toByteArray(),
            bootloader = "unknown".toByteArray(),
            fingerprint = model.finger(sdk).toByteArray(),
            bootId = generateUUID(getRandomByteArray(16, random).md5()).toByteArray(),
            procVersion = model.proc().toByteArray(),
            baseBand = model.baseBand.hexToBytes(),
            version = sdk.toDeviceVersion(),
            simInfo = "T-Mobile".toByteArray(),
            osType = "android".toByteArray(),
            macAddress = model.mac().toByteArray(),
            wifiBSSID = "02:00:00:00:00:00".toByteArray(),
            wifiSSID = "<unknown ssid>".toByteArray(),
            imsiMd5 = getRandomByteArray(16, random).md5(),
            imei = model.imei(),
            apn = "wifi".toByteArray()
        )
    }

    internal fun Model.finger(sdk: SdkVersion): String {
        return "${brand}/${device}/${device}:${sdk.release}/${display}/${sdk.incremental}:user/release-keys"
    }

    internal fun Model.imei(): String {
        val snr = getRandomIntString(6, random)
        val sp: Int = luhn(tac + fac + snr)
        return tac + fac + snr + sp
    }

    internal fun Model.proc(): String {
        return proc.ifBlank {
            "Linux version 3.0.31-${getRandomString(8, random)} (android-build@xxx.xxx.xxx.xxx.com)"
        }
    }

    internal fun Model.mac(): String {
        return if (mac.isNotBlank()) {
            mac + ':' +  getRandomByteArray(3, random).toUHexString(separator = ":")
        } else {
            val head = addr[brand]?.randomOrNull(random)
            if (head != null) {
                head + ':' +  getRandomByteArray(3, random).toUHexString(separator = ":")
            } else {
                "02:00:00:00:00:00"
            }
        }
    }

    internal fun SdkVersion.toDeviceVersion(): DeviceInfo.Version {
        return DeviceInfo.Version(
            incremental = incremental.toByteArray(),
            release = release.toByteArray(),
            codename = codename.toByteArray(),
            sdk = sdk,
        )
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
        val fac: String,
        val board: String,
        val device: String,
        val display: String,
        val model: String = "",
        val mac: String = "",
        val baseBand: String = "",
        val proc: String = "",
        val sdks: List<SdkVersion> = emptyList(),
    )

    @Serializable
    public data class SdkVersion(
        val incremental: String,
        val release: String,
        val codename: String,
        val sdk: Int,
    )
}