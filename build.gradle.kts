plugins {
    kotlin("jvm") version "1.6.0"
    kotlin("plugin.serialization") version "1.6.0"

    id("net.mamoe.mirai-console") version "2.10.0-RC2"
    id("net.mamoe.maven-central-publish") version "0.7.0"
}

group = "xyz.cssxsh.mirai"
version = "1.0.2"

mavenCentralPublish {
    useCentralS01()
    singleDevGithubProject("cssxsh", "mirai-device-generator")
    licenseFromGitHubProject("AGPL-3.0", "master")
    publication {
        artifact(tasks.getByName("buildPlugin"))
    }
}

repositories {
    mavenLocal()
    maven("https://maven.aliyun.com/repository/central")
    mavenCentral()
}

dependencies {
    compileOnly("net.mamoe:mirai-core-utils:2.10.0-RC2")

    testImplementation(kotlin("test", "1.6.0"))
}

kotlin {
    explicitApi()
}

mirai {
    configureShadow {
        exclude("module-info.class")
    }
}

tasks {
    test {
        useJUnitPlatform()
    }
}