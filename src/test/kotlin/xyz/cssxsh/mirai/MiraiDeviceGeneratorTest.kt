package xyz.cssxsh.mirai

import org.junit.jupiter.api.*

internal class MiraiDeviceGeneratorTest {

    @Test
    fun imei() {
        with(MiraiDeviceGenerator()) {
            println(models.random().imei())
        }
    }
}