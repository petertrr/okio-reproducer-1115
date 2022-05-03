plugins {
    kotlin("multiplatform") version "1.6.21"
}

repositories {
    mavenCentral()
}

kotlin {
    jvm()
    val nativeTargets = listOf(linuxX64(), mingwX64(), macosX64())

    sourceSets {
        val commonMain by getting
        val commonTest by getting
        val commonNonJsMain by creating {
            dependsOn(commonMain)
            dependencies {
                api("com.squareup.okio:okio:3.1.0")
            }
        }
        val commonNonJsTest by creating {
            dependsOn(commonTest)
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val jvmMain by getting {
            dependsOn(commonNonJsMain)
        }
        val jvmTest by getting {
            dependsOn(commonNonJsTest)
            dependencies {
                implementation(kotlin("test-junit5"))
                implementation("org.junit.jupiter:junit-jupiter-engine:5.8.2")
            }
        }
        nativeTargets.forEach {
            getByName("${it.name}Main").dependsOn(commonNonJsMain)
        }
        nativeTargets.forEach {
            getByName("${it.name}Test").dependsOn(commonNonJsTest)
        }
    }
}