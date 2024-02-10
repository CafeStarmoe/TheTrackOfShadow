import io.izzel.taboolib.gradle.*
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    id("io.izzel.taboolib") version "2.0.5"
    id("org.jetbrains.kotlin.jvm") version "1.9.22"
}

kotlin {
    sourceSets.all {
        languageSettings {
            languageVersion = "2.0"
        }
    }
}

taboolib {
    // 环境配置
    env {
        // 安装平台模块
        install(UNIVERSAL, BUKKIT_ALL)
        // 安装Raw 信息构建工具与 1.16 RGB 颜色转换
        install(CHAT)
        // 全平台配置文件解决方案 (Yaml & Toml & Hocon & Json)
        install(CONFIGURATION)
        // 全平台粒子绘制工具, 提供了大量基本函数
        install(EFFECT)
        // Bukkit 平台箱子菜单构建工具, 提供了数种不同类型的结构
        install(UI)
    }
    // 版本配置
    version {
        // TabooLib 版本
        taboolib = "6.1.0"
    }
    // 描述配置
    description {
        // 项目名称
        name("TheTrackOfShadow")
        // 项目描述
        desc("随影迷踪为YuCraft团队原创的基于Minecraft开发的PVP游戏。玩家需要在游戏内搜寻各种物资，强化自己的战斗能力，在最终决战击败其他对手获取胜利。")
        // 开发者列表
        contributors {
            // 描述仅在 Sponge 平台有效
            name("UnKownOwO").description("YuCraft团队开发者")
        }
        // 项目链接
        links {
            name("website").url("https://github.com/YuCraft/TheTrackOfShadow")
        }
        // api版本
        bukkitApi("1.20")
        // 插件依赖
        dependencies {
            // 全息投影
            name("HolographicDisplays").with("bukkit")
            // 协议库
            name("ProtocolLib").with("bukkit")
        }
    }
}

repositories {
    mavenCentral()
    maven("https://repo.codemc.io/repository/maven-public/")
    maven("https://oss.sonatype.org/content/repositories/snapshots")
    maven("https://repo.dmulloy2.net/repository/public/")
}

dependencies {
    compileOnly("ink.ptms.core:v12004:12004:mapped")
    compileOnly("ink.ptms.core:v12004:v12004:universal")
    compileOnly(kotlin("stdlib"))
    compileOnly(fileTree("libs"))
    compileOnly("net.md-5:bungeecord-api:1.20-R0.3-SNAPSHOT")
    compileOnly("me.filoghost.holographicdisplays:holographicdisplays-api:3.0.0")
    compileOnly("com.comphenix.protocol:ProtocolLib:5.1.0")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs = listOf("-Xjvm-default=all")
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}