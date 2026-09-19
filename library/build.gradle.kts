import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidKmpLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.vanniktech.mavenPublish)

    id ("signing")
}

val group = "com.steeplesoft"
val artifact = "camper"
val version = "0.3.3-SNAPSHOT"

kotlin {
    android {
        namespace = "com.steeplesoft.camper"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
        withHostTestBuilder {}
        packaging {
            resources {
                excludes += "/META-INF/{AL2.0,LGPL2.1}"
                excludes += "META-INF/INDEX.LIST"
            }
        }
    }

    compilerOptions {
        languageVersion = org.jetbrains.kotlin.gradle.dsl.KotlinVersion.KOTLIN_2_3
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
    }

    sourceSets {
        commonMain.dependencies {
                implementation(libs.compose.runtime)
                implementation(libs.compose.foundation)
                implementation(libs.compose.material3)
                implementation(libs.compose.material.icons.extended)
                implementation(libs.compose.ui)
                implementation(libs.compose.components.resources)
                implementation(libs.compose.uiToolingPreview)

                implementation(libs.kotlinx.datetime)
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation(libs.compose.ui.test)
        }
        getByName("androidHostTest") {
            dependencies {
                implementation(libs.kotlin.test.junit)
            }
        }
    }
}

configurations.named("androidHostTestCompileClasspath") {
    resolutionStrategy.dependencySubstitution {
        substitute(module("org.jetbrains.kotlin:kotlin-test"))
            .using(module("org.jetbrains.kotlin:kotlin-test-junit:${libs.versions.kotlin.get()}"))
    }
}


mavenPublishing {
    coordinates(group, artifact, version)

    pom {
        name = "Steeplesoft Camper"
        description =
            """
                |This library provides an easy-to-use and customizable solution for building forms in Kotlin Multiplatform applications.
                |This project is a fork of https://github.com/benjamin-luescher/compose-form.""".trimMargin()
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
                url = if (version.endsWith("-SNAPSHOT")) {
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
