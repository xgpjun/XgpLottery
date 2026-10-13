import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.api.attributes.java.TargetJvmVersion

plugins {
    kotlin("jvm") version "1.9.0"
    application
    id("com.github.johnrengelman.shadow") version "7.0.0"
}

group = "cn.xgpjun"
version = "2.1.0"
val nbtApiVersion = "2.16.0"

repositories {
    mavenCentral()
    exclusiveContent {
        forRepository {
            maven("https://repo.codemc.io/repository/maven-public/") {
                name = "CodeMC-NBTAPI"
            }
        }
        filter { includeGroup("de.tr7zw") }
    }
    maven("https://repo.helpch.at/releases/") {
        content { includeGroup("me.clip") }
    }
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://hub.spigotmc.org/nexus/content/repositories/public")
    maven("https://jitpack.io")
    maven("https://r.irepo.space/maven/")
    maven("https://repo.rosewooddev.io/repository/public/")
    maven("https://hub.spigotmc.org/nexus/content/groups/public/")
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://oss.sonatype.org/content/groups/public/")
    maven("https://repo.codemc.io/repository/nms/")
    maven("https://mvn.lumine.io/repository/maven-public/")
}

dependencies {
    implementation(kotlin("stdlib"))
    // Provided by the required NBTAPI server plugin; do not include it in shadowJar.
    compileOnly("de.tr7zw:item-nbt-api-plugin:$nbtApiVersion")
    compileOnly("net.kyori:examination-api:1.3.0")
    compileOnly("net.md-5:bungeecord-chat:1.20-R0.1")
    compileOnly("net.kyori:adventure-api:4.14.0")
//    {
//        targetConfiguration = "compile"
//    }
    compileOnly("net.kyori:adventure-key:4.4.0")
//    {
//        targetConfiguration = "compile"
//    }
    compileOnly("dev.folia:folia-api:1.20.1-R0.1-SNAPSHOT")
//    compileOnly("org.spigotmc:spigot-api:1.20.1-R0.1-SNAPSHOT")

//    compileOnly("org.spigotmc:spigot:1.12.2-R0.1-SNAPSHOT") // The full Spigot server with no shadowing. Requires mavenLocal.

    implementation("com.google.code.gson:gson:2.9.0")
    compileOnly("me.clip:placeholderapi:2.11.6")
    compileOnly("com.github.602723113:ParticleLib:1.5.1")
    compileOnly("net.dmulloy2:ProtocolLib:5.4.0")
    compileOnly("org.black_ixx:playerpoints:3.2.5")
    compileOnly("com.github.MilkBowl:VaultAPI:1.7")
    implementation("com.zaxxer:HikariCP:4.0.3")
    compileOnly(fileTree("lib"))
    compileOnly("pers.neige.neigeitems:NeigeItems:1.16.15")
    compileOnly("com.github.LoneDev6:api-itemsadder:3.5.0b")
    compileOnly("io.lumine:Mythic-Dist:5.3.5")
//    compileOnly("io.lumine:MythicLib-dist:1.5.2-SNAPSHOT")

}

tasks.test {
    useJUnitPlatform()
}

tasks.processResources {
    inputs.property("version", project.version)
    filesMatching("plugin.yml") {
        expand("version" to project.version)
    }
}

tasks.jar {
    archiveClassifier.set("plain")
}

// JDK 17 reads the provided modern server APIs during compilation. These APIs
// are not bundled; each server supplies its own version at runtime.
configurations.named("compileClasspath") {
    attributes.attribute(TargetJvmVersion.TARGET_JVM_VERSION_ATTRIBUTE, 17)
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
    kotlinOptions.freeCompilerArgs += "-Xjdk-release=1.8"
}

tasks.withType<JavaCompile> {
    options.release.set(8)
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

application {
    mainClass.set("cn.xgpjun.xgplottery2.XgpLotteryKt")
}

tasks{
    withType<ShadowJar>{
        archiveClassifier.set("")
        exclude("META-INF/maven/**")
        exclude("META-INF/tf/**")
        relocate("kotlin", "cn.xgpjun.kotlin")
        relocate("kotlinx","cn.xgpjun.kotlinx")
        relocate("_COROUTINE","cn.xgpjun._COROUTINE")
        relocate("DebugProbesKt.bin","cn.xgpjun")
        relocate("org.intellij","cn.xgpjun.org.intellij")
        relocate("org.jetbrains","cn.xgpjun.org.jetbrains")
        relocate("org.jetbrains","cn.xgpjun.org.jetbrains")
//        relocate("org.slf4j","cn.xgpjun.org.slf4j")
//        重定后启动会报错= =
        relocate("com.zaxxer","cn.xgpjun.com.zaxxer")
        relocate("com.google","cn.xgpjun.com.google")

    }
    build{
        dependsOn(shadowJar)
        //更新到服务端文件夹
//        dependsOn("copyJarToFolder")
    }
}

//更新到服务端文件夹
//val serverDir = file("D:\\桌面文件\\minecraft\\Server1.20.1")
//
//tasks.register("copyJarToFolder") {
//    dependsOn("jar") // 构建 JAR 之后执行
//    doLast {
//        val jarFile = tasks.getByName("jar").outputs.files.singleFile
//        val destinationDir = File(serverDir,"plugins")
//
//        copy {
//            from(jarFile)
//            into(destinationDir)
//        }
//    }
//}
