import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.vanniktech.mavenPublish)
    alias(libs.plugins.compose.compiler)

    id ("signing")
}

val group = "com.steeplesoft"
val artifact = "camper"
val version = "0.3.2-SNAPSHOT"

kotlin {
    jvmToolchain(11)
    jvm()
    androidTarget {
        publishLibraryVariants("release", "debug")
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "com.steeplesoft.camper"
            binaryOption("bundleId", "com.steeplesoft.camper")
            isStatic = true
        }
        it.compilations {
            val main by getting {
            }
        }
    }

    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }

    sourceSets {
        commonMain.dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.materialIconsExtended)
                implementation(compose.ui)
                implementation(compose.components.resources)
                implementation(compose.components.uiToolingPreview)

                implementation(libs.kotlinx.datetime)
        }
        commonTest.dependencies {
//                implementation(libs.kotlin.test)
        }
        jvmMain.dependencies {
        }
        jvmTest.dependencies {
        }
        androidMain.dependencies {
        }
        androidUnitTest.dependencies {
        }
        iosMain.dependencies {
        }
        iosTest.dependencies {
        }
    }
}

android {
    namespace = "com.steeplesoft.camper"
    buildToolsVersion = "36.0.0"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "META-INF/INDEX.LIST"

        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildFeatures {
        compose = true
    }
}

mavenPublishing {
    coordinates(group, artifact, version)

    pom {
        name = "Steeplesoft Camper"
        description =
            """
                |This library provides an easy-to-use and customizable solution for building forms in Kotlin Multiplatform applications.
                |This project is a fork of https://github.com/benjamin-luescher/compose-form, which appears to be abandoned."
            """.trimMargin()
        inceptionYear = "2023"
        url = "https://github.com/steeplesoft/camper"
        licenses {
            license {
                name = "MIT"
            }
        }
        developers {
            developer {
                id = "jasondlee"
            }
            developer {
                id = "edorex-luescher"
            }
        }
        scm {
            url = "https://github.com/steeplesoft/camper"
            connection = "scm:git:git@github.com:steeplesoft/camper.git"
            developerConnection = "scm:git:git@github.com:steeplesoft/camper.git"
        }
    }

    publishing {
        val localProps = gradleLocalProperties(rootDir, providers)
        repositories {
            maven {
                name = "Steeplesoft"
                credentials {
                    username = localProps["project.repoUsername"].toString()
                    password = localProps["project.repoPassword"].toString()
                }
                url = if (version.toString().endsWith("-SNAPSHOT")) {
                    uri(localProps["project.snapshotUrl"].toString())
                } else {
                    uri(localProps["project.releaseUrl"].toString())
                }
            }
        }
    }

    publishToMavenCentral()

    signAllPublications()
}

tasks.named("build") { finalizedBy("publishToMavenLocal") }
tasks.named("assemble") { finalizedBy("publishToMavenLocal") }
