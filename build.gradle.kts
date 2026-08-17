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



dependencies {
    intellijPlatform {
        rider("2026.2")
    }
}

intellijPlatform {
    pluginConfiguration {
        name = "Tokyo Night YuKi"
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
