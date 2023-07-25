package xyz.cssxsh.mirai.device

import kotlinx.serialization.*
import kotlinx.serialization.builtins.*
import kotlinx.serialization.json.*
import net.mamoe.mirai.*
import net.mamoe.mirai.utils.*
import kotlin.random.*

public class MiraiDeviceGenerator {

    @PublishedApi
    internal var random: Random = Random.Default

    @PublishedApi
    internal var models: List<Model>

    @PublishedApi
    internal var sdks: List<SdkVersion>

    @PublishedApi
    internal var addr: Map<String, List<String>>

    init {
        val clazz = MiraiDeviceGenerator::class.java
        models = Json.decodeFromString(
            ListSerializer(Model.serializer()),
            clazz.getResource("models.json")!!.readText()
        )
        sdks = Json.decodeFromString(
            ListSerializer(SdkVersion.serializer()),
            clazz.getResource("sdks.json")!!.readText()
        )
        addr = Json.decodeFromString(
            MapSerializer(String.serializer(), ListSerializer(String.serializer())),
            clazz.getResource("mac.json")!!.readText()
        )
    }

    @Suppress("INVISIBLE_REFERENCE", "INVISIBLE_MEMBER")
    public fun load(bot: Bot): DeviceInfo {
        val file = bot.configuration.workingDir.resolve("device.json")
        if (!file.exists() || file.length() == 0L) {
            return generate().also {
                file.writeText(DeviceInfoManager.serialize(it))
            }
        }
        return DeviceInfoManager.deserialize(file.readText())
    }

    public fun generate(): DeviceInfo {
        val model = models.random(random)
        val sdk = model.sdks.randomOrNull(random) ?: sdks.random(random)

        return DeviceInfoBuilder.fromRandom()
            .display(model.display)
            .product(model.name)
            .device(model.device)
            .board(model.board)
            .brand(model.brand)
            .model(model.model.ifEmpty { model.device })
            .fingerprint(model.finger(sdk))
            .procVersion(model.proc())
            .baseBand(model.baseBand.hexToBytes())
            .version(sdk.toDeviceVersion())
            .macAddress(model.mac())
            .imei(model.imei())
            .build()
    }

    // ro.build.fingerprint
    @PublishedApi
    internal fun Model.finger(sdk: SdkVersion): String {
        if (finger.isNotBlank()) return finger
        val id = sdk.id.ifBlank { display }
        val model = model.ifBlank { device }
        return "${brand}/${model}/${model}:${sdk.release}/${id}/${sdk.incremental}:user/release-keys"
    }

    @PublishedApi
    internal fun Model.imei(): String {
        val snr = getRandomIntString(6, random)
        val sp: Int = luhn(tac + fac + snr)
        return tac + fac + snr + sp
    }

    @PublishedApi
    internal fun Model.proc(): String {
        return proc.ifBlank {
            "Linux version 3.0.31-${getRandomString(8, random)} (android-build@xxx.xxx.xxx.xxx.com)"
        }
    }

    @PublishedApi
    internal fun Model.mac(): String {
        return if (mac.isNotBlank()) {
            mac + ':' + getRandomByteArray(3, random).toUHexString(separator = ":")
        } else {
            val head = addr[brand]?.randomOrNull(random)
            if (head != null) {
                head + ':' + getRandomByteArray(3, random).toUHexString(separator = ":")
            } else {
                "02:00:00:00:00:00"
            }
        }
    }

    @PublishedApi
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
        // 品牌名 Xiaomi / Huawei / ONEPLUS
        val brand: String,
        // imei 1~6 位
        val tac: String,
        // imei 7~8 位
        val fac: String,
        // 处理器代号 ro.board.platform
        val board: String,
        // device ~ model ro.product.model
        val device: String,
        //
        val model: String = "",
        // 操作系统版本号
        val display: String,
        //
        val mac: String = "",
        //
        val baseBand: String = "",
        //
        val proc: String = "",
        // ro.build.fingerprint
        val finger: String = "",
        //
        val sdks: List<SdkVersion> = emptyList(),
    )

    @Serializable
    public data class SdkVersion(
        // ro.build.id
        val id: String = "",
        // ro.build.version.incremental
        val incremental: String,
        // ro.build.version.release
        val release: String,
        // ro.build.version.codename
        val codename: String,
        // ro.build.version.sdk
        val sdk: Int,
    )
}