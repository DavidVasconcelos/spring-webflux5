import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import io.gitlab.arturbosch.detekt.Detekt

buildscript {
    val kotlinVersion: String by project
    repositories {
        maven {
            url = uri("https://plugins.gradle.org/m2/")
        }
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
    }
}

plugins {
    kotlin("jvm")
    kotlin("plugin.spring") apply false
    id("org.springframework.boot") apply false
    id("io.spring.dependency-management")
    id("io.gitlab.arturbosch.detekt") version ("1.21.0") apply false
}

java.sourceCompatibility = JavaVersion.VERSION_17

val projectGroup: String by project
val applicationVersion: String by project
allprojects {
    group = projectGroup
    version = applicationVersion

    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.jetbrains.kotlin.plugin.spring")
    apply(plugin = "org.jetbrains.kotlin.plugin.allopen")
    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "io.gitlab.arturbosch.detekt")

    dependencyManagement {
        val springCloudDependenciesVersion: String by project
        imports {
            mavenBom("org.springframework.cloud:spring-cloud-dependencies:${springCloudDependenciesVersion}")
        }
    }

    dependencies {
        val jacksonVersion: String by project
        implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")
        implementation("org.jetbrains.kotlin:kotlin-reflect")
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
        testImplementation("org.springframework.boot:spring-boot-starter-test") {
            exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
            exclude(group = "org.mockito", module = "mockito-core")
        }
    }

    tasks.getByName("bootJar") {
        enabled = false
    }

    tasks.getByName("jar") {
        enabled = true
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "17"
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
        ignoreFailures = false
        jvmArgs = listOf("-Duser.timezone=America/Sao_Paulo", "-XX:+UnlockExperimentalVMOptions")
    }

    tasks {
        val detektAll by registering(Detekt::class) {
            parallel = true
            setSource(files(projectDir))
            include("**/*.kt")
            include("**/*.kts")
            exclude("**/resources/**")
            exclude("**/build/**")
            config.setFrom(project.file("$rootDir/config/detekt.yml"))
            buildUponDefaultConfig = false
        }
    }
}
