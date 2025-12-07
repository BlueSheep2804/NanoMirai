import org.jetbrains.kotlin.parsing.parseBoolean

plugins {
    id("java-library")
    id("maven-publish")
    id("idea")
    id("net.neoforged.moddev") version "2.0.107"
    id("me.modmuss50.mod-publish-plugin") version "1.1.0"
    id("org.jetbrains.kotlin.jvm") version "2.0.0"
}

@Suppress("ConstPropertyName")
object ModInfo {
    const val minecraft_version = "1.21.1"
    const val minecraft_version_range = "[1.21.1,1.22)"
    const val neo_version = "21.1.208"
    const val neo_version_range = "[21,)"
    const val loader_version_range = "[5.3,)"
    const val parchment_minecraft_version = "1.21.1"
    const val parchment_mappings_version = "2024.11.17"
    const val mod_id = "nanomirai"
    const val mod_name = "NanoMirai"
    const val mod_license = "MIT"
    const val mod_version = "1.0.2"
    const val mod_group_id = "dev.bluesheep"
    const val mod_authors = "BlueSheep2804"
    const val mod_description = ""
    const val jei_version = "19.21.0.247"
    const val curios_version = "9.4.2+1.21.1"

}

val isSnapshot = parseBoolean(project.property("is_snapshot") as String)
val modVersion = if (isSnapshot) {
    "${ModInfo.mod_version}-SNAPSHOT"
} else {
    ModInfo.mod_version
}

version = modVersion
group = ModInfo.mod_group_id

base {
    archivesName = ModInfo.mod_id
}

java.toolchain.languageVersion = JavaLanguageVersion.of(21)
kotlin.jvmToolchain(21)

neoForge {
    version = ModInfo.neo_version

    parchment {
        mappingsVersion = ModInfo.parchment_mappings_version
        minecraftVersion = ModInfo.parchment_minecraft_version
    }

    // This line is optional. Access Transformers are automatically detected
    // accessTransformers.add('src/main/resources/META-INF/accesstransformer.cfg')

    runs {
        register("client") {
            client()
        }

        register("server") {
            server()
            programArgument("--nogui")
            gameDirectory = file("run-server")
        }

        register("data") {
            data()
            programArguments.addAll(
                "--mod", ModInfo.mod_id,
                "--all",
                "--output", file("src/generated/resources/").absolutePath,
                "--existing", file("src/main/resources/").absolutePath
            )
            gameDirectory = file("run-data")
        }

        configureEach {
            jvmArgument("-XX:+AllowEnhancedClassRedefinition")
            systemProperty("forge.logging.markers", "REGISTRIES")
            logLevel = org.slf4j.event.Level.DEBUG
        }
    }

    mods {
        register(ModInfo.mod_id) {
            sourceSet(sourceSets["main"])
        }
    }
}

sourceSets["main"].resources { srcDir("src/generated/resources") }

repositories {
    mavenLocal()
    maven {
        name = "Kotlin for Forge"
        url = uri("https://thedarkcolour.github.io/KotlinForForge/")
        content {
            includeGroup("thedarkcolour")
        }
    }
    maven {
        name = "ModMaven"
        url = uri("https://modmaven.dev/")
        content {
            includeGroup("mezz.jei")
        }
    }
    maven {
        name = "Illusive Soulworks maven"
        url = uri("https://maven.theillusivec4.top/")
        content {
            includeGroup("top.theillusivec4.curios")
        }
    }
}

dependencies {
    implementation("thedarkcolour:kotlinforforge-neoforge:5.9.0")

    compileOnly("mezz.jei:jei-${ModInfo.minecraft_version}-neoforge-api:${ModInfo.jei_version}")
    runtimeOnly("mezz.jei:jei-${ModInfo.minecraft_version}-neoforge:${ModInfo.jei_version}")

    compileOnly("top.theillusivec4.curios:curios-neoforge:${ModInfo.curios_version}:api")
    runtimeOnly("top.theillusivec4.curios:curios-neoforge:${ModInfo.curios_version}")
}

var generateModMetadata = tasks.register("generateModMetadata", ProcessResources::class.java) {
    var replaceProperties = mapOf(
        "minecraft_version" to ModInfo.minecraft_version,
        "minecraft_version_range" to ModInfo.minecraft_version_range,
        "neo_version" to ModInfo.neo_version,
        "neo_version_range" to ModInfo.neo_version_range,
        "loader_version_range" to ModInfo.loader_version_range,
        "mod_id" to ModInfo.mod_id,
        "mod_name" to ModInfo.mod_name,
        "mod_license" to ModInfo.mod_license,
        "mod_version" to modVersion,
        "mod_authors" to ModInfo.mod_authors,
        "mod_description" to ModInfo.mod_description
    )
    expand(replaceProperties)
    from("src/main/templates")
    into("build/generated/sources/modMetadata")
}

tasks.processResources {
    exclude("**/*.xcf", "**/*.aseprite")
}

sourceSets["main"].resources.srcDir(generateModMetadata)
neoForge.ideSyncTask(generateModMetadata)

idea {
    module {
        isDownloadSources = true
        isDownloadJavadoc = true
    }
}

publishMods {
    displayName = "${ModInfo.mod_name} $modVersion"
    changelog = file("changelog.md").readText()
    type = STABLE
    file = tasks.jar.get().archiveFile
    modLoaders.add("neoforge")

    //dryRun = true

    curseforge {
        accessToken = providers.environmentVariable("CURSEFORGE_API_KEY").getOrElse("")
        projectId = "1366797"
        minecraftVersions.add(ModInfo.minecraft_version)
        clientRequired = true
        serverRequired = true

        requires("kotlin-for-forge", "curios")
        optional("jei")
    }

    modrinth {
        accessToken = providers.environmentVariable("MODRINTH_TOKEN").getOrElse("")
        projectId = "XCjnR9PI"
        minecraftVersions.add(ModInfo.minecraft_version)

        requires("kotlin-for-forge", "curios")
        optional("jei")
    }
}
