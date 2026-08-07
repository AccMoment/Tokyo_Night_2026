plugins {
    id("org.jetbrains.intellij.platform") version "2.18.1"
}

group = "io.github.enkia"
version = "1.0.0"

repositories {
    mavenCentral()
    intellijPlatform {
        defaultRepositories()
    }
}

// 资源目录位于项目根 resources/（非标准 src/main/resources），显式声明
sourceSets {
    main {
        resources.srcDir("resources")
    }
}

dependencies {
    intellijPlatform {
        // 使用本机已安装的 Rider 作为 SDK（构建快，无需下载整个 SDK）
        // 若要在无 Rider 的机器上可移植构建，改为: rider("2026.2")
        local("C:/Users/AccMoment/AppData/Local/Programs/Rider")
    }
}

intellijPlatform {
    pluginConfiguration {
        name = "Tokyo Night Theme"
        ideaVersion {
            sinceBuild = "251"
        }
    }
    // 纯资源主题插件无 searchable options / 无 Java 代码 / 无需字节码插桩
    buildSearchableOptions = false
    instrumentCode = false
}

tasks {
    withType<Zip>().configureEach {
        // 保持 zip 内路径稳定（META-INF/ 在根）
        isPreserveFileTimestamps = false
        isReproducibleFileOrder = true
    }
}
