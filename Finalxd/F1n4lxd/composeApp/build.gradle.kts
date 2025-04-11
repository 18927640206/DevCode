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
            val desktopTest by getting {
                dependencies {
                    implementation(libs.junit.junit)
                    implementation(libs.junit.jupiter)

                    implementation(libs.mockk.jvm)

                }
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
            implementation(libs.jakarta.mail)
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
            implementation(libs.mysql.connector.java)
            implementation(libs.exposed.core)
            implementation(libs.exposed.dao)
            implementation(libs.exposed.jdbc)
            implementation(libs.jakarta.mail)
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
    useJUnitPlatform() // Usar JUnit 5
    testLogging {
        events("passed", "skipped", "failed")
    }
}