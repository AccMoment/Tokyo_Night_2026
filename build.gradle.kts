plugins {
    id("org.jetbrains.intellij.platform") version "2.18.1"
}

group = "io.github.enkia"
// 本地默认 1.0.1；CI 发版时可用 -Pversion=1.0.2 覆盖（见 .github/workflows/release.yml）
version = providers.gradleProperty("version").orNull ?: "1.0.1"

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

// CI（GitHub Actions 自动设置 CI=true）或显式传入 -PintellijPlatform.remoteSdk=true 时，
// 从 JetBrains 仓库下载 Rider SDK 构建；本地开发默认使用本机已安装的 Rider（构建快，无需下载）。
val useRemoteSdk: Boolean =
    providers.environmentVariable("CI").orNull == "true" ||
        providers.gradleProperty("intellijPlatform.remoteSdk").orNull == "true"

dependencies {
    intellijPlatform {
        if (useRemoteSdk) {
            rider("2026.2")
        } else {
            local("C:/Users/AccMoment/AppData/Local/Programs/Rider")
        }
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
