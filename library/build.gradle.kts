import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import com.android.tools.r8.internal.fi
import com.vanniktech.maven.publish.SonatypeHost
import org.bouncycastle.cms.RecipientId.password
import kotlin.math.sign
import kotlin.text.Typography.copyright

plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.vanniktech.mavenPublish)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.jreleaser)

    id ("signing")
}

val group = "com.steeplesoft"
val artifact = "kmp-form"
val version = libs.versions.kmpForm.get() //"0.3.0-SNAPSHOT"

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
    namespace = "com.steeplesoft.kmpform"
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
    coordinates(group, artifact, version)

    pom {
        name = "KMP Form"
        description =
            "This library provides an easy-to-use and customizable solution for building forms in Kotlin Multiplatform applications."
        inceptionYear = "2023"
        url = "https://github.com/steeplesoft/kmp-form"
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
            url = "https://github.com/steeplesoft/kmp-form"
            connection = "scm:git:git@github.com:steeplesoft/kmp-form.git"
            developerConnection = "scm:git:git@github.com:steeplesoft/kmp-form.git"
        }
    }

//    signAllPublications()

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
            maven {
                name = "PreDeploy"
                url = uri(layout.buildDirectory.dir("pre-deploy"))
            }
        }
    }
}

//build.finalizedBy(publishToMavenLocal)
tasks.named("build") { finalizedBy("publishToMavenLocal") }
tasks.named("assemble") { finalizedBy("publishToMavenLocal") }

/*
jreleaser {
    project {
        copyright = "jora.dev"
        description = "Gradle Publish Boilerplate project setup"
    }
    signing {
        setActive("ALWAYS")
        armored = true
        setMode ("FILE")
        publicKey = "public.key"
        secretKey = "private.key"
    }
    deploy {
        maven {
            mavenCentral {
                setActive("ALWAYS")
                create("sonatype") {
                    setActive("ALWAYS")
                    url = "https://central.sonatype.com/api/v1/publisher"
                    stagingRepository("build/staging-deploy")
//                    setUserName (findProperty("mavenCentralUsername") ?: System.getenv("OSSRH_USERNAME"))
//                    password = findProperty("mavenCentralPassword") ?: System.getenv("OSSRH_PASSWORD")
                    stagingRepository("build/pre-deploy")
                }
            }
        }
    }
    release {
        github {
            enabled = false
        }
    }
}
*/
