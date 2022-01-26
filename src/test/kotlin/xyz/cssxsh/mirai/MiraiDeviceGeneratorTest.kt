package xyz.cssxsh.mirai

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class MiraiDeviceGeneratorTest {

    @Test
    fun imei() {
        with(MiraiDeviceGenerator()) {
            println(models.random().imei())
        }
    }
}