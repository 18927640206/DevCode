import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

repositories {
    maven("https://repo.repsy.io/mvn/chrynan/public") // Mockative repo
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev") // Compose
}

kotlin {
    jvm("desktop")

    sourceSets {
        val desktopMain by getting

        val commonTest by getting {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }

        val desktopTest by getting {
                dependencies {
                    implementation(libs.junit.junit)
                    implementation(libs.junit.jupiter)
                    implementation(libs.mockk.jvm)
                    implementation(libs.slf4j.simple)
                    implementation(libs.junit.jupiter.engine)
                }
        }

        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)
            implementation(libs.mysql.connector.java)
            implementation(libs.exposed.core)
            implementation(libs.exposed.dao)
            implementation(libs.exposed.jdbc)
            implementation(libs.voyager.navigator)
            implementation(libs.voyager.tabNavigator)
            implementation(libs.voyager.screenModel)
            implementation(libs.voyager.transitions)
            implementation(libs.voyager.bottomSheetNavigator)

            implementation(libs.compose.material)
            implementation(compose.components.resources)


        }

        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)

            implementation(libs.mysql.connector.java)
            implementation(libs.exposed.core)
            implementation(libs.exposed.dao)
            implementation(libs.exposed.jdbc)
            implementation("com.itextpdf:itextpdf:5.5.13.3")
            implementation ("com.google.zxing:core:3.4.1")
            implementation ("com.google.zxing:javase:3.4.1")
            implementation(compose.desktop.currentOs)
            implementation(libs.junit.junit)
            implementation(libs.junit.jupiter)
            implementation(kotlin("test"))
            implementation("org.junit.jupiter:junit-jupiter-api:5.10.2")
            implementation("org.junit.jupiter:junit-jupiter-engine:5.10.2")
            implementation("io.mockk:mockk:1.13.10") // versión estable para JVM
            implementation("org.slf4j:slf4j-simple:2.0.9")

        }
    }
}

compose.desktop {
    application {
        mainClass = "org.luisitobez.burgerved.MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "org.luisitobez.burgerved"
            packageVersion = "1.0.0"
        }
    }
}

tasks.withType<Test> {

    useJUnitPlatform() // Esto es necesario para usar JUnit 5
    jvmArgs("-XX:+EnableDynamicAgentLoading")
    testLogging {
        events("passed", "skipped", "failed")
    }
}