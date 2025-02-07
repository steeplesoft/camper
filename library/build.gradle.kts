import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.vanniktech.mavenPublish)
    alias(libs.plugins.compose.compiler)
}

val group = "com.github.benjamin-luescher"
val artifact = "compose-form"
val version = "0.3.0"

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
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.compilations {
            val main by getting {
            }
        }
    }

    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }

//    sourceSets.all {
//        val suffixIndex = name.indexOfLast { it.isUpperCase() }
//        val targetName = name.substring(0, suffixIndex)
//        val suffix = name.substring(suffixIndex).lowercase().takeIf { it != "main" }
//        println("SOURCE_SET: $name")
//        kotlin.srcDir("$targetName/${suffix ?: "src"}")
//        resources.srcDir("$targetName/${suffix?.let { it + "Resources" } ?: "resources"}")
//    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.foundation)
                implementation(compose.runtime)
                implementation(compose.material3)
                implementation(compose.ui)
                implementation(compose.materialIconsExtended)

                implementation(libs.kotlinx.datetime)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }
        val jvmMain by getting {
        }
        val jvmTest by getting
        val androidMain by getting {
            dependsOn(commonMain)
        }
        val androidUnitTest by getting {
            dependsOn(commonTest)
        }
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
        }
        val iosX64Test by getting
        val iosArm64Test by getting
        val iosSimulatorArm64Test by getting
        val iosTest by creating {
            dependsOn(commonTest)
            iosX64Test.dependsOn(this)
            iosArm64Test.dependsOn(this)
            iosSimulatorArm64Test.dependsOn(this)
        }
    }
}

android {
    namespace = "ch.benlu.composeform"
    buildToolsVersion = "35.0.0"
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
    publishing {
        val localProps = gradleLocalProperties(rootDir, providers)
        repositories {
            maven {
                credentials {
                    username = localProps["project.repoUsername"].toString()
                    password = localProps["project.repoPassword"].toString()
                }
                println("project.version = $version")
                if (version.toString().endsWith("-SNAPSHOT")) {
                    url = uri(localProps["project.snapshotUrl"].toString())
                } else {
                    url = uri(localProps["project.releaseUrl"].toString())
                }
                println("url = $url")
            }
        }
    }

    coordinates(group.toString(), artifact, version.toString())

    pom {
        name = "Compose Form"
        description =
            "This library provides an easy-to-use and customizable solution for building forms in Android Jetpack Compose."
        inceptionYear = "2023"
        url = "https://github.com/benjamin-luescher/compose-form"
        licenses {
            license {
                name = "MIT"
            }
        }
        developers {
            developer {
                id = "edorex-luescher"
            }
        }
        scm {
            url = "https://github.com/benjamin-luescher/compose-form"
            connection = "scm:git:git@github.com:benjamin-luescher/compose-form.git"
            developerConnection = "scm:git:git@github.com:benjamin-luescher/compose-form.git"
        }
    }
}
