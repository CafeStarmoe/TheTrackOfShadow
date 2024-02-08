import io.izzel.taboolib.gradle.*
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    id("io.izzel.taboolib") version "2.0.2"
    id("org.jetbrains.kotlin.jvm") version "1.8.22"
}

taboolib {
    // 环境配置（例如模块、仓库地址等）
    // 此处列出所有可用选项，但通常均可省略
    env {
        // 调试模式
        debug = false
        // 是否在开发模式下强制下载依赖
        forceDownloadInDev = true
        // 中央仓库地址
        repoCentral = "https://maven.aliyun.com/repository/central"
        // TabooLib 仓库地址
        repoTabooLib = "http://ptms.ink:8081/repository/releases"
        // 依赖下载目录
        fileLibs = "libraries"
        // 资源下载目录
        fileAssets = "assets"
        // 是否启用隔离加载器（即完全隔离模式）
        enableIsolatedClassloader = false
        // 安装模块
        install(UNIVERSAL)
        // 安装配置文件
        install(CONFIGURATION)
        // 安装 Bukkit 平台实现
        install(BUKKIT)
        // 安装 Bukkit 拓展工具
        install(BUKKIT_UTIL, BUKKIT_XSERIES)
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
    // 此处列出所有可用选项，除 "TabooLib 版本" 外均省略
    version {
        // TabooLib 版本
        taboolib = "6.1.0"
        // Kotlinx Coroutines 版本（设为 null 表示禁用）
        coroutines = "1.7.3"
        // 跳过 Kotlin 加载
        skipKotlin = false
        // 跳过 Kotlin 重定向
        skipKotlinRelocate = false
        // 跳过 TabooLib 重定向
        skipTabooLibRelocate = false
    }
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
        // 项目前缀
        prefix("TheTrackOfShadow")
        // 插件加载阶段
        // START 表示这个插件在服务器启动时就开始加载。
        // POSTWORLD 表示这个插件在第一个世界加载完成后开始加载。
        load("POSTWORLD")
        // api版本
        bukkitApi("1.20")
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
    maven("https://repo.dmulloy2.net/repository/public/")
    maven("https://oss.sonatype.org/content/repositories/snapshots")
}

dependencies {
    compileOnly("ink.ptms.core:v12004:12004:mapped")
    compileOnly("ink.ptms.core:v12004:v12004:universal")
    compileOnly(kotlin("stdlib"))
    compileOnly(fileTree("libs"))
    compileOnly("me.filoghost.holographicdisplays:holographicdisplays-api:3.0.0")
    compileOnly("com.comphenix.protocol:ProtocolLib:5.1.0")
    compileOnly("net.md-5:bungeecord-api:1.20-R0.3-SNAPSHOT")
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

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}