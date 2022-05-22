plugins {
    kotlin("jvm") version "1.6.21"
    kotlin("plugin.serialization") version "1.6.21"

    id("net.mamoe.mirai-console") version "2.11.0"
    id("net.mamoe.maven-central-publish") version "0.7.1"
}

group = "xyz.cssxsh.mirai"
version = "1.1.0"

mavenCentralPublish {
    useCentralS01()
    singleDevGithubProject("cssxsh", "mirai-device-generator")
    licenseFromGitHubProject("AGPL-3.0", "master")
    publication {
        artifact(tasks.getByName("buildPlugin"))
        artifact(tasks.getByName("buildPluginLegacy"))
    }
}

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    compileOnly("net.mamoe:mirai-core-utils:2.11.0")

    testImplementation(kotlin("test", "1.6.21"))
}

kotlin {
    explicitApi()
}

tasks {
    test {
        useJUnitPlatform()
    }
}