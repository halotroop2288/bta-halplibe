plugins {
	id("xyz.wagyourtail.unimined") version("1.4.1")
	`maven-publish`
}

group = project.properties["mod_group"].toString()
base.archivesName = project.properties["mod_name"].toString()
version = project.properties["mod_version"].toString()

repositories {
	mavenLocal()
	mavenCentral()

	unimined.wagYourMaven("snapshots")
	unimined.signalumMaven("infrastructure")
	unimined.jitpack()
}

unimined {
	useGlobalCache = false // TODO: Fix online BTA manifest and remove this

	bta {
		version(project.properties["bta_version"].toString())

		// Recommended configuration for updated dependencies
		fabric {
			loader(project.properties["loader_version"].toString())
			accessWidener(file("src/main/resources/halplibe.accesswidener"))
		}
	}
}

dependencies {
	implementation("net.betterthanadventure:FabricGameProvider:1.0.0:client")
	implementation("net.betterthanadventure:FabricGameProvider:1.0.0:server")
	implementation("com.github.Better-Than-Adventure:legacy-lwjgl3:1.0.6")
	listOf("SoundSystem", "LibraryJavaSound", "CodecJOrbis", "CodecWAV", "CodecMus").forEach {
		implementation("com.paulscode:$it:1.0.0-SNAPSHOT")
	}
}

java {
	sourceCompatibility = JavaVersion.VERSION_1_8
	targetCompatibility = JavaVersion.VERSION_1_8
	withSourcesJar()
}

tasks.jar.configure {
	from("LICENSE") {
		rename { "${it}_${base.archivesName}" }
	}
}

tasks.withType(ProcessResources::class).configureEach {
	inputs.property("version", version)

	filesMatching("fabric.mod.json") {
		expand("version" to version)
	}
}

publishing {
	repositories {
		maven("https://maven.thesignalumproject.net/releases") {
			name = "Signalum"
			credentials(PasswordCredentials::class)
			authentication {
			}
		}
	}

	publications {
	}
}
