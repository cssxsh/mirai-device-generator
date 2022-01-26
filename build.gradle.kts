plugins {
    kotlin("jvm") version "1.6.0"
    kotlin("plugin.serialization") version "1.6.0"

    id("net.mamoe.mirai-console") version "2.10.0-RC2"
}

group = "xyz.cssxsh.mirai"
version = "1.0.0-dev"

repositories {
    mavenLocal()
    maven("https://maven.aliyun.com/repository/central")
    mavenCentral()
}

dependencies {
    compileOnly("net.mamoe:mirai-core-utils:${mirai.coreVersion}")

    testImplementation(kotlin("test", kotlin.coreLibrariesVersion))
}

kotlin {
    explicitApi()
}

mirai {
    jvmTarget = JavaVersion.VERSION_11
    configureShadow {
        exclude("module-info.class")
    }
}

tasks {
    test {
        useJUnitPlatform()
    }
}