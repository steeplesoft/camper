import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.vanniktech.mavenPublish)
    alias(libs.plugins.jetbrainsCompose)
}

val group = "com.github.benjamin-luescher"
val artifact = "compose-form"
val version = "0.3.0-Fork-SNAPSHOT"

kotlin {
    jvm()
    androidTarget {
        publishLibraryVariants("release")
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_1_8)
        }
    }
    iosX64()
    iosArm64()
    iosSimulatorArm64()

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

        iosMain.dependencies {
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.runtime)
            implementation(compose.ui)
            implementation(compose.materialIconsExtended)
        }

    }
}

android {
    namespace = "org.jetbrains.kotlinx.multiplatform.library.template"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
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

    signAllPublications()

    coordinates(group.toString(), artifact, version.toString())

    pom {
        name = "Compose Form"
        description = "This library provides an easy-to-use and customizable solution for building forms in Android Jetpack Compose."
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
